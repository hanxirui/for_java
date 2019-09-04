package am.hanxirui.bootdemo.elasticsearch.one;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Map;
import java.util.Set;

/**
 * ElasticSearch查询条件操作工具类
 * @author zchuanzhao
 * 2018/7/5
 */
public class EsDataGridUtils {

    /**
     * 排序字段
     */
    private static final String SORT_FIELD = "sortField";

    /**
     * 排序类型
     */
    private static final String SORT_ORDER = "sortOrder";

    /**
     * 查询字段
     */
    private static final String QUERY_KEY = "query";

    /**
     * 相等条件
     */
    private static final String EQUALS_KEY = "equals";

    /**
     * 模糊匹配条件
     */
    private static final String LIKE_KEY = "like";

    /**
     * 大于等于条件
     */
    private static final String GREATER_EQUALS_KEY = "ge";

    /**
     * 小于等于条件
     */
    private static final String LESS_EQUALS_KEY = "le";

    /**
     * 查询条件关系
     */
    private static final String SELECT_KEY = "select";

    /**
     * AND条件关系
     */
    private static final String AND_KEY = "and";
    /**
     * ASC
     */
    private static final String ASC_KEY = "asc";

    /**
     *  解析通用查询参数（分页参数、排序参数）
     * @param builder
     * @param queryInfo 查询信息
     * @param beanClass 查询对象
     * @param defaultSort 默认排序
     */
    public static void parseQueryInfo(SearchRequestBuilder builder, QueryInfo queryInfo, Class beanClass, String defaultSort) {
        Map<String, String> requestMap = queryInfo.getRequestMap();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 排序类型
        SortOrder sortOrder = SortOrder.DESC;
        // 排序字段
        String sortField = defaultSort;
        // 查询字段
        if (requestMap != null) {
            //获取排序信息
            if (requestMap.containsKey(SORT_ORDER)) {
                String sortOrderValue = requestMap.get(SORT_ORDER);
                if (ASC_KEY.equals(sortOrderValue)){
                    sortOrder = SortOrder.ASC;
                }
            }
            if (requestMap.containsKey(SORT_FIELD) && null !=  requestMap.get(SORT_FIELD)) {
                sortField = requestMap.get(SORT_FIELD);
            }
            //获取查询条件信息
            if (requestMap.containsKey(QUERY_KEY)) {
                String queryStr = requestMap.get(QUERY_KEY);
                if(!StringUtils.isEmpty(queryStr)) {
                    JSONObject queryObj = JSONObject.fromObject(queryStr);
                    // 相等的字段
                    JSONObject equalsObj = queryObj.optJSONObject(EQUALS_KEY);
                    // 匹配字段
                    JSONObject likeObj = queryObj.optJSONObject(LIKE_KEY);
                    // 匹配字段
                    JSONObject geObj = queryObj.optJSONObject(GREATER_EQUALS_KEY);
                    // 匹配字段
                    JSONObject leObj = queryObj.optJSONObject(LESS_EQUALS_KEY);
                    if(equalsObj != null) {
                        Set<String> set = equalsObj.keySet();
                        // 字段值
                        Object objVal;
                        // 字段值
                        Object fieldVal;
                        for (String fieldName: set) {
                            fieldVal = equalsObj.opt(fieldName);
                            // 若字段为枚举，返回枚举值，否则直接返回字段值
                            objVal = EnumUtils.getValByField(beanClass, fieldName, fieldVal);
                            boolQueryBuilder.must(QueryBuilders.matchQuery(fieldName, objVal));
                        }
                    }
                    JSONObject selectObj = queryObj.optJSONObject(SELECT_KEY);
                    boolean queryAnd = false;
                    if (null != selectObj) {
                        queryAnd = (Boolean) selectObj.get(AND_KEY);
                    }
                    if(likeObj != null) {
                        Set<String> set = likeObj.keySet();
                        for(String key: set) {
                            QueryBuilder termQuery = QueryBuilders.termQuery(key, likeObj.optString(key));
                            if (queryAnd){
                                boolQueryBuilder.must(termQuery);
                            }else {
                                boolQueryBuilder.should(termQuery);
                            }
                        }
                    }
                    //大于等于
                    if(geObj != null) {
                        Set<String> set = geObj.keySet();
                        for(String key: set) {
                            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(key).gte(geObj.optString(key));
                            if (queryAnd){
                                boolQueryBuilder.must(rangeQuery);
                            }else {
                                boolQueryBuilder.should(rangeQuery);
                            }
                        }
                    }
                    //小于等于
                    if(leObj != null) {
                        Set<String> set = leObj.keySet();
                        for(String key: set) {
                            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(key).lte(leObj.optString(key));
                            if (queryAnd){
                                boolQueryBuilder.must(rangeQuery);
                            }else {
                                boolQueryBuilder.should(rangeQuery);
                            }
                        }
                    }
                }
            }
        }
        builder.setQuery(boolQueryBuilder);

        //分页查询，设置偏移量和每页查询数量
        builder.setFrom(queryInfo.getCurrentPageNum() * queryInfo.getCountPerPage()).setSize(queryInfo.getCountPerPage());

        //排序
        builder.addSort(sortField, sortOrder);
    }
}