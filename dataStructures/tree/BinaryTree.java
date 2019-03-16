package dataStructures.tree;

/**
 * 二叉树(不能存在重复值)
 * 左子树的值 < 父节点 < 右子树的值
 */
public class BinaryTree {

    class Node {

        public int data;
        public Node left;
        public Node right;
        public Node parent;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    private Node root;

    /**
     * 插入节点
     * @param value
     */
    public void put(int value) {
        if (root == null) {
            root = new Node(value);
        } else {
            Node parent = find(value);
            if (value < parent.data) {
                parent.left = new Node(value);
                parent.left.parent = parent;
            } else if(value > parent.data){
                parent.right = new Node(value);
                parent.right.parent = parent;
            } else {
                throw new RuntimeException("树中已存在该值，不能重复");
            }
        }
    }

    /**
     * 查找节点，找不到则返回父节点
     * @param value
     * @return
     */
    public Node find(int value) {
        Node current = root;
        while (current != null) {
            if (current.data > value) {
                if (current.left == null)
                    return current;
                current = current.left;
            } else if (current.data < value) {
                if (current.right == null)
                    return current;
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    public void inOrder(Node root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.data + "\t");
            inOrder(root.right);
        }
    }

    public void preOrder(Node root) {
        if (root != null) {
            System.out.print(root.data + "\t");
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    public void postOrder(Node root) {
        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.print(root.data + "\t");
        }
    }

    /**
     * 删除节点并返回被删除节点，包含三种情况
     * 第一种情况: 被删除节点是叶子节点
     * 第二种情况: 被删除节点只包含左子树或右子树
     * 第三种情况: 被删除节点同时包含左右子树
     * @param value 要删除节点的值
     * @return 被删除节点
     */
    public Node remove(int value) {
        Node node = find(value);
        //第三种情况，当删除节点同时包含左右子树时，需要找到后继节点来替换被删除节点（把被删除节点的值替换为后继结点的值），
        //这样问题就转换为被删除的节点只包含一个子树或被删除的节点是叶子节点的简单情况
        if (node.left != null && node.right != null) {
            Node successor = findSuccessor(node.right);
            int temp = successor.data;
            successor.data = node.data;
            node.data = temp;
            node = successor;
        }
        Node child;
        if (node.left == null) {
            child = node.right;
        } else {
            child = node.left;
        }
        if (child == null) { //第一种情况，删除节点是叶子节点
            if (node.parent.left != null && node.parent.left.data == value) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
        } else { //第二种情况，删除节点只有左子树或右子树
            if (node.left == null) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.left;
            }
        }
        return node;
    }

    /**
     * 查找后继结点
     * <p>当删除节点同时包含左右子树时，右子树中最靠左的节点可以用来替换被删节点</p>
     * @param n 右子树的根节点
     * @return 后继节点
     */
    public Node findSuccessor(Node n) {
        if (n.left == null) {
            return n;
        }
        return findSuccessor(n.left);
    }

    public static void main(String[] args) {
        //准备数据
        int[] nums = {4, 3, 1, 23, 9, 11};
        BinaryTree binaryTree = new BinaryTree();
        for (int num : nums) {
            binaryTree.put(num);
        }

        System.out.println("====================1. 搜索节点====================");
        System.out.println(binaryTree.find(1));

        System.out.println("====================2. 二叉树的遍历====================");
        System.out.print("中序排序 : ");
        binaryTree.inOrder(binaryTree.root);
        System.out.println();
        System.out.print("前序排序 : ");
        binaryTree.preOrder(binaryTree.root);
        System.out.println();
        System.out.print("后序排序 : ");
        binaryTree.postOrder(binaryTree.root);
        System.out.println();

        System.out.println("====================3. 删除节点====================");
        System.out.println("1) 找继承节点 : ");
        Node node = binaryTree.findSuccessor(binaryTree.root.right);
        System.out.println(node);

        System.out.println("2) 同时存在左右子节点 -> 删除4,结果如下 : ");
        Node remove1 = binaryTree.remove(4);
        System.out.println("被删除节点为 : " + remove1);
        binaryTree.inOrder(binaryTree.root);
        System.out.println();

        System.out.println("3) 存在左节点或右节点 -> 删除23,结果如下 : ");
        Node remove2 = binaryTree.remove(23);
        System.out.println("被删除节点为 : " + remove2);
        binaryTree.inOrder(binaryTree.root);
        System.out.println();

        System.out.println("4) 叶子节点，删除1 -> 结果如下 : ");
        binaryTree.remove(1);
        binaryTree.inOrder(binaryTree.root);
        System.out.println();
    }

}
