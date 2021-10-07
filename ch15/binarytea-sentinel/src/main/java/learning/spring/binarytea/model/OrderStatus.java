package learning.spring.binarytea.model;

public enum OrderStatus {
    ORDERED(0), PAID(1), MAKING(2), FINISHED(3), TAKEN(4);

    private int index;

    OrderStatus(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
