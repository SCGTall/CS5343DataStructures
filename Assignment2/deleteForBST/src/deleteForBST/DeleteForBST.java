/**
 * FileName: DeleteForBST.java
 * Introduction:
 * 1. Create a BST using the following values , inserted in the order given here.
 *
 *     100, 50, 200, 150, 300, 25, 75, 12, 37, 125, 175, 250, 320, 67, 87, 94, 89, 92, 88
 *
 * 2. Do an inorder traversal to print the values.
 *
 * 3. delete 100, use predecessor.
 *
 * 4. Do an  inorder traversal again.
 *
 * submit the code
 *
 * Submit screen shot of each execution of the code.
 *
 * Thought:
 * 1. I will realize a BST with function of insert, inorder traversal and delete;
 * 2. First, I will design a insert and then use insert to complete the BST;
 * 3. My inorder traversal (left, node, right) will print the result automatical;
 * 4. For delete, I will find the target and the predecessor, then copy the predecessor's value to target and delete
 * the predecessor.
 *
 * @author Chaoran Li
 * @Date 09/22/2019
 * @version 1.0
 */

package deleteForBST;

public class DeleteForBST {
    public static class MyNode{
        int value;
        MyNode left;
        MyNode right;
    }

    public static class MyBST{
        public MyNode root;

        /**
         * public method to insert using a private methods
         * @param inValue insert value
         */
        public void insertBST(int inValue){
            if (root == null){
                root = new MyNode();
                root.value = inValue;
                return;
            }
            insertBSTRecur(root, inValue);
        }

        /**
         * prvate recursion method for insertBST
         * @param node target node
         * @param inValue insert value
         */
        private void insertBSTRecur(MyNode node, int inValue){
            if (inValue < node.value){
                if (node.left == null){
                    node.left = new MyNode();
                    node.left.value = inValue;
                    return;
                }
                insertBSTRecur(node.left, inValue);
            }
            else{
                if (node.right == null){
                    node.right = new MyNode();
                    node.right.value = inValue;
                    return;
                }
                insertBSTRecur(node.right, inValue);
            }
        }

        /**
         * public method to do an inorder traversal (left, node, right) using a private methods
         */
        public void inOrderTrav(){
            StringBuilder result = new StringBuilder();
            inOrderTravRecur(root, result);
            System.out.printf("inorder traversal: [" + result.toString() + "]\n");
        }

        /**
         * prvate recursion method for inOrderTrav
         * @param node target node
         */
        private void inOrderTravRecur(MyNode node, StringBuilder result){
            if (node == null) return;
            inOrderTravRecur(node.left, result);
            if (result.length() > 0){
                result.append(", ");
            }
            result.append(node.value);
            inOrderTravRecur(node.right, result);
            return;
        }

        /**
         * public method to delete a number in BST, using predecessor
         * @param value target delete number
         */
        public void delete(int value){
            // find the target
            MyNode target = root;
            MyNode parent = null;// record the parent for delete
            while (target != null){
                if (target.value == value) break;
                parent = target;
                target = value < target.value ? target.left : target.right;
            }
            if (target == null) return;
            // find the predecessor and delete
            if (target.left != null) {
                MyNode pred = target.left;
                if (pred.right == null){// left child itself
                    target.value = pred.value;
                    target.left = pred.left;
                }
                else{// left subtree's most right child
                    MyNode predParent = pred;
                    pred = pred.right;
                    while (pred.right != null){
                        predParent = pred;
                        pred = pred.right;
                    }
                    target.value = pred.value;
                    predParent.right = pred.left;
                }
            }
            else{// if target have no left subtree, the predecessor may be its parent
                if (parent == null){
                    root = target.right;
                }
                else{
                    parent.right = target.right;
                }
            }
        }
    }

    public static void main(String[] args) {
        // init
        int a[] = new int[]{100, 50, 200, 150, 300, 25, 75, 12, 37, 125, 175, 250, 320, 67, 87, 94, 89, 92, 88};// input
        MyBST bst = new MyBST();
        // insert
        System.out.printf("Init BST now.\n");
        for (int i = 0; i < a.length; i++){
            bst.insertBST(a[i]);
        }
        // traversal
        bst.inOrderTrav();
        // delete 100, using predecessor
        System.out.printf("Delete 100, using predecessor.\n");
        bst.delete(100);
        // traversal again
        bst.inOrderTrav();
    }
}
