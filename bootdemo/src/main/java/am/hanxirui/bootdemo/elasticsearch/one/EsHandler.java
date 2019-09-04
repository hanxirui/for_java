package am.hanxirui.bootdemo.elasticsearch.one;

import com.google.common.collect.Lists;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * ElasticSearch工具类
 *
 * @author zchuanzhao
 * 2018/7/3
 */
public class EsHandler {
    /**
     * 需要分词字段
     */
    private static List<String> ANALYZED_FIELD = Lists.newArrayList("userName", "authType");
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
    private static final int ES_TCP_PORT = 9300;

    public ThreadLocal<SearchResponse> responseThreadLocal = new ThreadLocal<>();

    private static final Map<String, String> MAPPINGS = new HashMap<>();

    static Settings settings = Settings.builder().put("cluster.name", ES_CLUSTER_NAME).build();
    // 创建ES客户端
    private static TransportClient client;

    static {
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(ES_HOST), ES_TCP_PORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得ES客户端
     */
    public static TransportClient getClient() {
        return client;
    }

    /**
     * 关闭客户端
     *
     * @param client
     */
    public static void close(TransportClient client) {
        if (null != client) {
            client.close();
        }
    }

    /**
     * Mapping处理
     *
     * @param obj
     */
    public static void mapping(Object obj, String indexName) {
        String type = obj.getClass().getSimpleName().toLowerCase();
        if (!MAPPINGS.containsKey(type)) {
            synchronized (EsHandler.class) {
                createIndex(indexName);
                //获取所有的Mapping
                ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().getIndices().get(ES_CLUSTER_NAME).getMappings();
                MappingMetaData mmd = mappings.get(type);
                if (null == mmd) {
                    createMapping(obj);
                } else {
                    CompressedXContent xContent = mappings.get(type).source();
                    if (xContent == null) {
                        createMapping(obj);
                    } else {
                        String mapping = xContent.toString();
                        MAPPINGS.put(type, mapping);
                    }
                }
            }
        }
    }

    /**
     * 创建Mapping
     *
     * @param obj
     */
    public static void createMapping(Object obj) {
        String type = obj.getClass().getSimpleName().toLowerCase();
        PutMappingRequest mapping = Requests.putMappingRequest(ES_CLUSTER_NAME).type(type).source(setMapping(obj));
        EsHandler.getClient().admin().indices().putMapping(mapping).actionGet();
    }

    /**
     * 设置对象的ElasticSearch的Mapping
     *
     * @param obj
     * @return
     */
    public static XContentBuilder setMapping(Object obj) {
        List<Field> fieldList = getFields(obj);
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder().startObject().startObject("properties");
            for (Field field : fieldList) {
                //修饰符是static的字段不处理
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String name = field.getName();
                if (ANALYZED_FIELD.contains(name)) {
                    mapping.startObject(name).field("type", getElasticSearchMappingType(field.getType().getSimpleName().toLowerCase())).field("index", "analyzed")
                            //使用IK分词器
                            .field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").endObject();
                } else {
                    mapping.startObject(name).field("type", getElasticSearchMappingType(field.getType().getSimpleName().toLowerCase())).field("index", "not_analyzed").endObject();
                }
            }
            mapping.endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * 获取对象所有自定义的属性
     *
     * @param obj
     * @return
     */
    private static List<Field> getFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(fields));
        Class objClass = obj.getClass();
        while (null != objClass) {
            fieldList.addAll(Arrays.asList(objClass.getDeclaredFields()));
            objClass = objClass.getSuperclass();
        }
        return fieldList;
    }

    /**
     * java类型与ElasticSearch类型映射
     *
     * @param varType
     * @return
     */
    private static String getElasticSearchMappingType(String varType) {
        String es;
        switch (varType) {
            case "date":
                es = "date";
                break;
            case "double":
                es = "double";
                break;
            case "long":
                es = "long";
                break;
            case "int":
                es = "long";
                break;
            default:
                es = "string";
                break;
        }
        return es;
    }

    /**
     * 判断ElasticSearch中的索引是否存在
     */
    private static boolean indexExists(String indexName) {
        IndicesAdminClient adminClient = client.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(indexName);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建索引
     */
    private static void createIndex(String indexName) {
        if (!indexExists(indexName)) {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            client.admin().indices().create(request);
        }
    }

    private static String[] getIndices() {
        IndicesAdminClient adminClient = client.admin().indices();
        String[] indices = adminClient.getIndex(new GetIndexRequest()).actionGet().getIndices();
        return indices;
    }

    public static void main(String[] args) {
        String[] indices = getIndices();
        for (String index : indices) {
            System.out.println(index);
        }

    }
}