package namingtool;

// Just a sample

public class FileTemplate {
    private String pattern;
    private String notes;
    private String fileName;
    
    public FileTemplate(String name, String notes){
        this.pattern = name;
        this.fileName = name;
        this.notes = notes;
        
    }
    
    public void replace(String newValue, String oldValue){
        fileName = pattern.replace(newValue, oldValue);
    }

    public void clear(){
        fileName = pattern;
    }
    
    public String getName(){
        return fileName;
    }
    
    public String getNotes(){
        return notes;
    }
    

}
