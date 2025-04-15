public class Node<K, V> {
    /**
     * Кеш-код ключа.
     */
    final int hash;

    /**
     * Ключ.
     */
    final K key;

    /**
     * Значение.
     */
    V value;

    /**
     * Ссылка на следующий элемент в bucket'е.
     */
    Node<K, V> next;

    /**
     * Конструктор с параметрами.
     *
     * @param hash  - хэш-код ключа
     * @param key   - ключ
     * @param value - значение
     * @param node  - ссылка на следующий элемент в bucket'е
     */
    public Node(int hash, K key, V value, Node<K, V> node) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = node;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<K, V> node = (Node<K, V>) o;
        return (getKey() == node.getKey() || (getKey() != null && getKey().equals(node.getKey())))
                && (getValue() == node.getValue() || (getValue() != null && getValue().equals(node.getValue())));
    }

    @Override
    public final int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
    }

    @Override
    public final String toString() {
        return getKey() + ", " + getValue();
    }
}
