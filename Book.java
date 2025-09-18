class Book {
    private String id;
    private String name;
    private int quantity;
    private boolean isBorrowed;

    public Book(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.isBorrowed = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
