package ru.vsu.cs.skofenko;

import java.util.*;

public class Treap<T extends Comparable<T>> implements Iterable<T> {
    private static final Random RND = new Random();

    private static class Node<T extends Comparable<T>> {
        private T x;
        private final int y;

        private int size = 1;

        private Node<T> left;
        private Node<T> right;

        public Node(T x, int y, Node<T> left, Node<T> right) {
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
            this.size = getSize(this.left) + getSize(this.right) + 1;
        }

        public Node(T x) {
            this.x = x;
            this.y = RND.nextInt();
        }
    }

    private static <T extends Comparable<T>> int getSize(Node<T> node) {
        return node == null ? 0 : node.size;
    }

    private static class Pair<T extends Comparable<T>> {
        private Node<T> less;
        private Node<T> greater;

        public Pair(Node<T> less, Node<T> greater) {
            this.less = less;
            this.greater = greater;
        }
    }

    private Node<T> root = null;
    private int size = 0;

    //в первой все ключи <= чем во второй
    private static <T extends Comparable<T>> Node<T> merge(Node<T> first, Node<T> second) {
        if (first == null || second == null)
            return first == null ? second : first;
        if (first.y > second.y) {
            var newRight = merge(first.right, second);
            return new Node<>(first.x, first.y, first.left, newRight);
        } else {
            var newLeft = merge(first, second.left);
            return new Node<>(second.x, second.y, newLeft, second.right);
        }
    } //O(h)

    //в первую <key, во вторую >= key
    private static <T extends Comparable<T>> Pair<T> split(Node<T> root, T key) {
        if (root == null) {
            return new Pair<>(null, null);
        }
        if (root.x.compareTo(key) < 0) {
            /*
             *       1=less
             *    left    1` 2`
             *
             * */
            Pair<T> pair = split(root.right, key);
            return new Pair<>(new Node<>(root.x, root.y, root.left, pair.less), pair.greater);
        } else {
            /*
             *     2=greater
             *    1` 2`  right
             *
             * */
            Pair<T> pair = split(root.left, key);
            return new Pair<>(pair.less, new Node<>(root.x, root.y, pair.greater, root.right));
        }
    } // O(h)

    private void add(T value) {
        Pair<T> pair = split(root, value);
        pair.less = merge(pair.less, new Node<>(value));
        root = merge(pair.less, pair.greater);
        size++;
    }

    public T put(T value) {
        Node<T> temp = getNode(value);
        if (temp == null) {
            add(value);
            return null;
        } else {
            temp.x = value; //св-ва BST не нарушатся, т.к. они equal
            return temp.x;
        }
    }

    public T remove(T value) {
        if (root == null)
            return null;
        if (root.x.compareTo(value) == 0) {
            T temp = root.x;
            root = merge(root.left, root.right);
            size--;
            return temp;
        }
        Node<T> prev = null;
        Node<T> curr = root;
        Queue<Boolean> turnedLeft = new LinkedList<>();
        while (curr != null) {
            int sw = curr.x.compareTo(value);
            if (sw == 0)
                break;
            else {
                prev = curr;
                if (sw < 0) {
                    curr = curr.right;
                    turnedLeft.add(false);
                } else {
                    curr = curr.left;
                    turnedLeft.add(true);
                }
            }
        }
        if (curr == null)
            return null;
        if (prev.left != null && prev.left.equals(curr)) {
            prev.left = merge(curr.left, curr.right);
        } else {
            prev.right = merge(curr.left, curr.right);
        }
        T temp = curr.x;
        curr = root;
        while (!turnedLeft.isEmpty()) {
            curr.size--;
            curr = turnedLeft.poll() ? curr.left : curr.right;
        }
        size--;
        return temp;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        return size;
    }

    private Node<T> getNode(T value) {
        Node<T> curr = root;
        while (curr != null) {
            int sw = curr.x.compareTo(value);
            if (sw == 0)
                return curr;
            else if (sw < 0)
                curr = curr.right;
            else
                curr = curr.left;
        }
        return null;
    }

    public T get(T value) {
        var res = getNode(value);
        if (res != null)
            return res.x;
        return null;
    }

    // можно разбить на less, greater, equal, но там константа хуже, лучше как в обычном BST
    public boolean contains(T value) {
        return get(value) != null;
    }

    public static <T extends Comparable<T>> Iterable<T> inOrderValues(Node<T> treeNode) {
        return () -> {
            Stack<Node<T>> stack = new Stack<>();
            Node<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    Node<T> node = stack.pop();
                    T result = node.x;
                    if (node.right != null) {
                        node = node.right;
                        while (node != null) {
                            stack.push(node);
                            node = node.left;
                        }
                    }
                    return result;
                }
            };
        };
    }

    @Override
    public Iterator<T> iterator() {
        return inOrderValues(root).iterator();
    }

    public T findInOrderKey(int k) {
        if (k < 0 || k >= size)
            return null;
        Node<T> curr = root;
        while (curr != null) {
            int lSize = getSize(curr.left);
            if (lSize == k)
                return curr.x;
            else if (lSize > k)
                curr = curr.left;
            else {
                k -= 1 + lSize;
                curr = curr.right;
            }
        }
        return null;
    }
}
