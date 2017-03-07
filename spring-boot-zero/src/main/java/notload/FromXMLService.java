package notload;

import org.springframework.stereotype.Service;

/**
 * Created by hanxirui on 2017/2/23.
 */
@Service
public class FromXMLService {
    /**
     * 启动的时候观察控制台是否打印此信息;
     */
    public FromXMLService() {
        System.out.println("----------------------FromXMLService");
    }
}
