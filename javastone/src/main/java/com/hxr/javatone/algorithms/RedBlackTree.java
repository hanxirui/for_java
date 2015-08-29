package com.hxr.javatone.algorithms;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 1.每个结点要么是红色要么是黑色； 2.根结点必须是黑色； 3.每个叶节点,即空节点(NULL)是黑色的。 4.红结点如果有孩子，其孩子必须都是黑色(红节点周边3个节点都是黑色)；
 * 5.从根结点到叶子的每条路径必须包含相同数目的黑结点。
 * <p>
 * Create on : 2015年8月27日<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author hanxirui<br>
 * @version javastone v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class RedBlackTree {
    
    private static final int BLACK = 1;
    private static final int RED = 0;
    
    /**
     * @param args
     */
    public static void main(final String[] args) {
        // 1.建立一棵树，并进行初始化，也可通过插入节点进行建树
        // 插入结点操作
        RedBlackNode nil = new RedBlackNode(null, null, null, -1, BLACK);//设定哨兵节点
        creatTree(nil);// 通过插入结点创建树，并以哨兵节点的父指针指向根节点
        showTree(nil.pNode, nil);// 展示插入建树后的结果
        deleteTree(nil, 8);// 删除节点，删除的节点也可从外部传入
        System.out.println("删除结点后：");
        showTree(nil.pNode, nil);// 展示删除树结点的结果
    }
    private static void creatTree(final RedBlackNode nil) {
        // 新建NIL[T]结点
        int[] number = new int[] { 1, 2,3, 5, 7, 8, 11, 12,14, 15, 16,17,19,19,20,21,23,26,28,30,35,38,39,41,47,50,55,57,59 };
        // int[] number = new int[] { 8,11,17,15,6,1,22,25,27 };
        for (int i = 0; i < number.length; i++) {
            int inputValue = number[i];
            insert(inputValue, nil);
        }
    }

    public static void initTree(final RedBlackNode node, final RedBlackNode nil) {
        //初始化构造树int[] a={1,2,5,7,8,11,14,15,4}
        RedBlackNode tempnode = new RedBlackNode(nil, nil, nil, -1, BLACK);
        node.leftNode = new RedBlackNode(nil, nil, node, 2, RED);
        node.rightNode = new RedBlackNode(nil, nil, node, 14, BLACK);

        tempnode = node.leftNode;
        tempnode.leftNode = new RedBlackNode(nil, nil, tempnode, 1, BLACK);
        tempnode.rightNode = new RedBlackNode(nil, nil, tempnode, 7, BLACK);

        tempnode = tempnode.rightNode;
        tempnode.leftNode = new RedBlackNode(nil, nil, tempnode, 5, RED);
        tempnode.rightNode = new RedBlackNode(nil, nil, tempnode, 8, RED);

        tempnode = node.rightNode;
        tempnode.rightNode = new RedBlackNode(nil, nil, tempnode, 15, RED);
        /*
         * int i = 2; while (i < 23) { if ((i == 2) || (i == 3) || (i == 4) ||
         * (i == 6) || (i == 7) || (i == 9) || (i == 11) || (i == 12) || (i ==
         * 14) || (i == 17) || (i == 18) || (i == 19) || (i == 20) || (i == 22))
         * node[i] = new RedBlackNode(nil, nil, nil, i, 1); i++; } // 赋值
         * node[7].setLeftNode(node[4]); node[7].setRightNode(node[11]);
         * node[4].setLeftNode(node[3]); node[4].setRightNode(node[6]);
         * node[4].setPNode(node[7]); node[3].setLeftNode(node[2]);
         * node[3].setPNode(node[4]); node[2].setPNode(node[3]);
         * node[6].setPNode(node[4]);
         * 
         * node[11].setLeftNode(node[9]); node[11].setRightNode(node[18]);
         * node[11].setPNode(node[7]);
         * 
         * node[18].setLeftNode(node[14]); node[18].setRightNode(node[19]);
         * node[18].setPNode(node[11]);
         * 
         * node[14].setLeftNode(node[12]); node[14].setRightNode(node[17]);
         * node[14].setPNode(node[18]);
         * 
         * node[19].setRightNode(node[22]); node[19].setPNode(node[18]);
         * 
         * node[22].setLeftNode(node[20]); node[22].setPNode(node[19]);
         * 
         * node[12].setPNode(node[14]); node[17].setPNode(node[14]);
         * node[20].setPNode(node[22]); // show(node);
         * 
         */
    }

    public static void showTree(final RedBlackNode node, final RedBlackNode nil) {
        // 中序循环遍历树
        /*
         * JavaStack stack = new JavaStack(); // 将根结点输入栈中 while (node != nil ||
         * !stack.IsNull()) { if (node != nil) { stack.push(node); node =
         * node.leftNode; } else { RedBlackNode tempnode = (RedBlackNode) stack.pop();
         * System.out.print(tempnode.key+" " ); if (tempnode.rightNode != nil)
         * node = tempnode.rightNode; } }
         */
        // 层次遍历，分别打印出树的根结点,不用递归用循环提高效率
        
        Queue<RedBlackNode> queue = new LinkedBlockingQueue<RedBlackNode>();
        queue.add(node);// 将根结点输入栈中
        while (!queue.isEmpty()) {//队列不为空则出队列，继续添加新节点
            /**/
            RedBlackNode tempnode = queue.poll();
            int key = tempnode.key;
            StringBuffer str = new StringBuffer();//通过StringBuffer进行输出
            str.append("key:" + key);
            if (tempnode.color == 0) {
                str.append(" color:红色");
            } else {
                str.append(" color:黑色");
            }
            if (tempnode.leftNode != nil && tempnode.leftNode != null) {
                str.append(" lefttree:" + tempnode.leftNode.key);
                queue.add(tempnode.leftNode);
            }

            if (tempnode.rightNode != nil) {
                str.append(" righttree:" + tempnode.rightNode.key);
                queue.add(tempnode.rightNode);
            }
            System.out.println(str.toString());
        }
    }

    public static void leftRotate(final RedBlackNode xnode, final RedBlackNode nil) {
        //左旋转的实际为改变节点的前趋后继,三个节点，当前节点，当前节点的右节点，当前节点右节点的左节点
        RedBlackNode yNode = xnode.rightNode;// 设置右孩子为y结点
        xnode.rightNode = yNode.leftNode;
        if (yNode.leftNode != nil)
            yNode.leftNode.pNode = xnode;
        yNode.pNode = xnode.pNode;
        if (xnode.pNode == nil) {
            nil.pNode = yNode;
        } else if (xnode == xnode.pNode.leftNode) {
            xnode.pNode.leftNode = yNode;
        } else {
            xnode.pNode.rightNode = yNode;
        }
        yNode.leftNode = xnode;
        xnode.pNode = yNode;
    }

    public static void rightRotate(final RedBlackNode xnode, final RedBlackNode nil) {
        RedBlackNode yNode = xnode.leftNode;// 设置左孩子为y结点
        xnode.leftNode = yNode.rightNode;
        if (yNode.rightNode != nil) {

            yNode.rightNode.pNode = xnode;
        }
        yNode.pNode = xnode.pNode;
        if (xnode.pNode == nil) {
            nil.pNode = yNode;
        } else if (xnode == xnode.pNode.rightNode) {
            xnode.pNode.rightNode = yNode;
        } else {
            xnode.pNode.leftNode = yNode;
        }
        yNode.rightNode = xnode;
        xnode.pNode = yNode;
    }

    public static void insert(final int i, final RedBlackNode nil) {
        RedBlackNode newnode = new RedBlackNode(nil, nil, nil, i, RED);// 设置新插入结点初始值为i
        RedBlackNode tempnode = new RedBlackNode(nil, nil, nil, -1, RED);// 设置比较结点的父结点
        RedBlackNode recnode = new RedBlackNode(nil, nil, nil, -1, RED);// 设置一个循环树结点
        // 执行查找过程，找到相应的位置并进行插入，随后进行红黑性质的插入调整
        recnode = nil.pNode;
        if (recnode == null) {
            nil.pNode = newnode;
            newnode.color = BLACK;
        } else {
            while (recnode != nil) {
                tempnode = recnode;
                if (i < recnode.key) {
                    recnode = recnode.leftNode;
                } else {
                    recnode = recnode.rightNode;
                }
            }
            newnode.pNode = tempnode;
            if (newnode.key < tempnode.key) {
                tempnode.leftNode = newnode;
            } else {
                tempnode.rightNode = newnode;
            }
            // 执行修补程序
            InsertRedBlackNodeFixup(nil, newnode);
        }
    }

    public static void InsertRedBlackNodeFixup(final RedBlackNode nil, RedBlackNode newnode) {
        RedBlackNode tempnode = new RedBlackNode(nil, nil, nil, -1, RED);
        /*执行插入主要解决红红冲突，具体情况有三种，注意插入的结点默认均为红色
         * 1.newnode 的叔叔为红,即将其父，叔结点均置为黑
         * 2.newnode 的叔叔是黑，newnode为父结点的右孩子
         * 3.newnode 的叔叔是黑，newnode为父结点的左孩子
         * 2,3的解决办法是先进行一次相应方向的旋转(左子树即左转，右子树即右转)，然后将newnode设为祖父结点，
         * 进行相应的反方向转动，最后设置根结点值为黑色
         * 插入操作可通过两次旋转彻底解决平衡问题
        */
        while (newnode.pNode.color == 0) {
            if (newnode.pNode == newnode.pNode.pNode.leftNode) {
                tempnode = newnode.pNode.pNode.rightNode;// 叔父结点为爷爷的左子树
                if (tempnode.color == 0) {// 叔父结点为红
                    newnode.pNode.color = BLACK;
                    tempnode.color = BLACK;
                    newnode.pNode.pNode.color = RED;
                    newnode = newnode.pNode.pNode;
                } else {
                    if (newnode == newnode.pNode.rightNode)// 叔父结点为黑并为其父爷点的左子树
                    {
                        newnode = newnode.pNode;
                        leftRotate(newnode, nil);
                    }
                    newnode.pNode.color = BLACK;
                    newnode.pNode.pNode.color = RED;
                    rightRotate(newnode.pNode.pNode, nil);
                }
            } else {
                tempnode = newnode.pNode.pNode.leftNode;// 叔父结点为爷爷的右子树
                if (tempnode.color == 0) {// 叔父结点为红
                    newnode.pNode.color = BLACK;
                    tempnode.color = BLACK;
                    newnode.pNode.pNode.color = RED;
                    newnode = newnode.pNode.pNode;
                } else // 叔父结点为黑并为其父爷点的左子树
                {
                    if (newnode == newnode.pNode.leftNode) {
                        newnode = newnode.pNode;
                        rightRotate(newnode, nil);
                    }
                    newnode.pNode.color = BLACK;
                    newnode.pNode.pNode.color = RED;

                    leftRotate(newnode.pNode.pNode, nil);
                }
            }
        }
        nil.pNode.color = BLACK;
    }

 

    public static void deleteTree(final RedBlackNode nil, final int i) {
        /*删除结点主要考虑三个问题：
         * 1.删除的为叶子节点，即左右子树为空
         * 2.删除的为带一棵子树的结点
         * 前面两种情况可以合并讨论，都是直接让父结点指向孩子结点或空节点(左右子树为空时)，删除后设置
         * 旋转结点，进行修复
         * 3.删除的为有左右子树的结点，需要取得其后继结点，作为新结点
 */
        // 查找到删除节点，从根结点开始循环遍历
        RedBlackNode zNode = new RedBlackNode(nil, nil, nil, i, BLACK);
        RedBlackNode xNode = new RedBlackNode(nil, nil, nil, -1, BLACK);
        RedBlackNode tempNode = nil.pNode;// 设置根结点
        RedBlackNode rootnode= new RedBlackNode(nil, nil, nil, -1, BLACK);

        boolean mark = false;// 查找对象结点
        while (tempNode != nil && mark == false) {
            if (zNode.key < tempNode.key) {
                tempNode = tempNode.leftNode;
            } else if (zNode.key > tempNode.key) {
                tempNode = tempNode.rightNode;
            } else {
                zNode = tempNode;
                mark = true;
            }
        }
        // zNode即为当前节点，进行删除操作
        RedBlackNode yNode = new RedBlackNode(nil, nil, nil, -1, BLACK);//y结点用于指示被删除节点
        if ((zNode.leftNode == nil) || (zNode.rightNode == nil)) {
            yNode = zNode;// 用yNode指向将被删除的结点
        } else {
            yNode = lookAfterNode(nil, zNode);// 寻找后继节点
        }
        // 进行删除，通过后继与父母结点之间建立双向关系进行删除
        if (yNode.leftNode != nil) {
            xNode = yNode.leftNode;
        } else {
            xNode = yNode.rightNode;
        }
        if ((zNode.leftNode == nil) && (zNode.rightNode == nil)) {
            rootnode=xNode.pNode;//此时指向空值时，nil的父母节点会改变，所以在此保留根结点，最后恢复时可用
        }
        xNode.pNode = yNode.pNode;
        // //////////////////////////////////////////

        if (yNode.pNode == nil) {
            nil.pNode = xNode;
        } else if (yNode == yNode.pNode.leftNode) {
            yNode.pNode.leftNode = xNode;
        } else {
            yNode.pNode.rightNode = xNode;
        }
        if (yNode != zNode) {
            zNode.key = yNode.key;
        }
        if (yNode.color == BLACK) {//解决黑黑冲突，如果为红则不需要改变
            deleteTreeFixUp(nil, xNode);
        }
        nil.pNode=rootnode;//还原根结点为nit的父母结点
    }

    public static RedBlackNode lookAfterNode(final RedBlackNode nil, RedBlackNode node) {
        RedBlackNode returnNode = new RedBlackNode(nil, nil, nil, -1, RED);
        if (node.rightNode != nil) {
            returnNode = node.rightNode;
            while (returnNode.leftNode != nil) {
                returnNode = returnNode.leftNode;
            }
            return returnNode;
        }
        returnNode = node.pNode;
        while (returnNode != nil && node == returnNode.rightNode) {
            node = returnNode;
            returnNode = node.pNode;
        }
        return returnNode;
    }

    public static void deleteTreeFixUp(final RedBlackNode nil, RedBlackNode xNode) {
        /*  删除修复：
         *  1.x的兄弟w是红色的
         *  2.x的兄弟w是黑色的，而且w的两个孩子都是黑色的
         *  3.x的兄弟w是黑色的，而且w的左孩子为红，右孩子为黑
         *  4.x的兄弟w是黑色的，而且w的左孩子为黑，右孩子为红
         *  */
        RedBlackNode adjustNode = new RedBlackNode(nil, nil, nil, -1, BLACK);
        while (xNode != nil.pNode && xNode.color == BLACK) {
            if (xNode == xNode.pNode.leftNode) {
                adjustNode = xNode.pNode.rightNode;
                if (adjustNode.color == 0) {
                    adjustNode.color = BLACK;
                    xNode.pNode.color = RED;
                    leftRotate(xNode.pNode, nil);
                    adjustNode = xNode.pNode.rightNode;
                }
                if (adjustNode.leftNode.color == BLACK
                        && adjustNode.rightNode.color == BLACK) {
                    adjustNode.color = RED;
                    xNode = xNode.pNode;
                } else if (adjustNode.rightNode.color == BLACK) {
                    adjustNode.leftNode.color = BLACK;
                    adjustNode.color = RED;
                    rightRotate(adjustNode, nil);
                    adjustNode = xNode.pNode.rightNode;
                }
                adjustNode.color = adjustNode.pNode.color;
                adjustNode.pNode.color = BLACK;
                adjustNode.rightNode.color = BLACK;
                leftRotate(xNode.pNode, nil);
                xNode = nil.pNode;
            } else {
                adjustNode = xNode.pNode.leftNode;
                if (adjustNode.color == 0) {
                    adjustNode.color = BLACK;
                    xNode.pNode.color = RED;
                    rightRotate(xNode.pNode, nil);
                    adjustNode = xNode.pNode.leftNode;
                }
                if (adjustNode.rightNode.color == BLACK
                        && adjustNode.leftNode.color == BLACK) {
                    adjustNode.color = RED;
                    xNode = xNode.pNode;
                } else if (adjustNode.leftNode.color == BLACK) {
                    adjustNode.rightNode.color = BLACK;
                    adjustNode.color = RED;
                    leftRotate(adjustNode, nil);
                    adjustNode = xNode.pNode.leftNode;
                }
                adjustNode.color = adjustNode.pNode.color;
                adjustNode.pNode.color = BLACK;
                adjustNode.leftNode.color = BLACK;
                rightRotate(xNode.pNode, nil);
                xNode = nil.pNode;
            }
        }
        xNode.color = BLACK;
    }
}

class RedBlackNode {

        //应该包含五个域,left,right,p,key,color;
        RedBlackNode leftNode;//指示左结点
        RedBlackNode rightNode;//指示右结点
        RedBlackNode pNode;//指示父结点或者NIL(T)哨兵结点
        int key;//结点的值
        int color;//其中0表示红，1表示黑
        public RedBlackNode getLeftNode() {
            return leftNode;
        }
        public void setLeftNode(final RedBlackNode leftNode) {
            this.leftNode = leftNode;
        }
        public RedBlackNode getRightNode() {
            return rightNode;
        }
        public void setRightNode(final RedBlackNode rightNode) {
            this.rightNode = rightNode;
        }
        public RedBlackNode getPNode() {
            return pNode;
        }
        public void setPNode(final RedBlackNode node) {
            pNode = node;
        }
        public int getKey() {
            return key;
        }
        public void setKey(final int key) {
            this.key = key;
        }
        public int getColor() {
            return color;
        }
        public void setColor(final int color) {
            this.color = color;
        }
        //默认构造函数
        public RedBlackNode(final RedBlackNode leftNode,final RedBlackNode rightNode,final RedBlackNode pNode,final int  key,final int color){
            this.leftNode=leftNode;
            this.rightNode=rightNode;
            this.pNode=pNode;
            this.key=key;
            this.color=color;
        }
    

}
