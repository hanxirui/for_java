
if (!window.ZIndexMgr){

/**
 * css样式中z-index管理器，用于发放和回收z-index数值
 * @class ZIndexMgr
 */
var ZIndexMgr = (function(){
	
	if (getParent() != window && getParent().ZIndexMgr) {
		return getParent().ZIndexMgr
	}
    
    var S_INIT_INDEX_I = 55555,
        m_indexList_a = [];
    
     return {
         /**
          * 获取z-index数值
          * 
          *     // 举例
          *     var zIndex = ZIndexMgr.get();
          *     
          * @method get
          * @return {number} z-index数值
          */
         get : getIndex_fn,
         
         /**
          * 回收z-index数值
          * 
          *     // 举例
          *     ZIndexMgr.free($div);
          *     
          * @method free
          * @param domObj {object} dom对象或jquery对象，使用该z-index数值的对象
          */
         free : destroy_fn,
         
         toString : function () {
             return "[" + m_indexList_a.join(', ') + "]";
         }
     }
     
     function getIndex_fn(){
         var index;
         if (0 === m_indexList_a.length) {
             index = S_INIT_INDEX_I;
         }else {
             index = m_indexList_a[m_indexList_a.length - 1] + 1;
         }
         
         m_indexList_a.push(index);
                  
         return index;
     }
     
     function destroy_fn(domObj){
         var i, 
             len = m_indexList_a.length,
             index = $(domObj).css("z-index"),
             index_i,
             found = false;
         
         if (undefined === index) return;
         
         index_i = parseInt(index);
         
         // 查找
         for (i = 0; i < len; i += 1) {
             if (m_indexList_a[i] === index_i) {
                 found = true;
                 break;
             }
         }
         
         // 整理
         for (;i < len; i += 1){
             if (i == len - 1) {
                 m_indexList_a.splice(len - 1);
             }else {
                 m_indexList_a[i] = m_indexList_a[i + 1];
             }
         }
         
         $(domObj).css('z-index', 0);
         
     }
     
     
 })(); 


}