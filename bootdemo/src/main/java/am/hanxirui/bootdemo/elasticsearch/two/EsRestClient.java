package am.hanxirui.bootdemo.elasticsearch.two;

import com.google.common.io.ByteStreams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public class EsRestClient {
    private static Log log = LogFactory.getLog(EsRestClient.class);
    /**
     * 集群名称cluster.name
     */
    public static final String ES_CLUSTER_NAME = "elasticsearch";
    /**
     * 主机IP
     */
    private static final String ES_HOST = "172.16.3.88";
    /**
     * 端口号
     */
    private static final int ES_HTTP_PORT = 9200;

    // 创建ES客户端
    private static RestClient restClient;

    public static RestClient getRestClient() {
        if (restClient != null) {
            return restClient;
        } else {
            try {
                RestClientBuilder builder = RestClient.builder(new HttpHost(ES_HOST, ES_HTTP_PORT, "http"));
                Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
                builder.setDefaultHeaders(defaultHeaders);
                builder.setMaxRetryTimeoutMillis(10000);
                builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
                builder.setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(Node node) {
                        log.error(node.getHost().getHostName());
                    }
                });


                restClient = builder.build();
                return restClient;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public static void getIndices(RestClient restClient) {
        Request request = new Request("GET", "_cat/indices");
//        You can add request parameters to the request object
        request.addParameter("pretty", "true");

//        You can set the body of the request to any HttpEntity:
//        request.setEntity(new NStringEntity(
//                "{\"json\":\"text\"}",
//                ContentType.APPLICATION_JSON));

//        The RequestOptions class holds parts of the request that should be shared between many requests in the same application. You can make a singleton instance and share it between all requests:
//        RequestOptions.Builder optBuilder = RequestOptions.DEFAULT.toBuilder();
//        optBuilder.addHeader("Authorization", "Bearer ");
//        optBuilder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
//        RequestOptions COMMON_OPTIONS = optBuilder.build();
//        request.setOptions(COMMON_OPTIONS);

        try {
            Response response = restClient.performRequest(request);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
//            byte[] bytes = new byte[content.available()];
//            content.read(bytes, 0, bytes.length);
//            log.info(new String(bytes, "UTF-8"));

            byte[] targetArray = ByteStreams.toByteArray(content);
            System.out.println(new String(targetArray, "UTF-8"));

            content.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        return indices;
    }

    public static void addDocuments(HttpEntity[] documents) {
        final CountDownLatch latch = new CountDownLatch(documents.length);
        for (int i = 0; i < documents.length; i++) {
            Request request = new Request("PUT", "/posts/doc/" + i);
            //let's assume that the documents are stored in an HttpEntity array
            request.setEntity(documents[i]);
            restClient.performRequestAsync(request, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {

                    latch.countDown();
                }

                @Override
                public void onFailure(Exception exception) {

                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RestClient restClient = getRestClient();
        getIndices(restClient);
//        String[] indices = getIndices();
//        for (String index : indices) {
//            System.out.println(index);
//        }

        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
