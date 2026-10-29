class Column {
    private final String name;
    private final String type;

    public Column(String name, String type){
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException(
                "Column name cannot be empty"
            );
        }
        if (!"INT".equals(type) && !"VARCHAR".equals(type)){
            throw new IllegalArgumentException(
                "Unsupported column type : "+type
            );
        }
        this.name = name;
        this.type = type;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
}