package namingtool;

import java.util.ArrayList;


// How to change the list?
public class ResultList {
    private ArrayList<FileTemplate> list;
    
    public void replace(ArrayList<Substitution> substitutions){
        //TODO implement replace for each file in list 
        //                               each change in substitutions
    }
    
    public ResultList(){
        list = new ArrayList<FileTemplate>();
        list.add(new FileTemplate("YYYYMMDD_document_version", "Date = expiration"));
        list.add(new FileTemplate("Subject_report", ""));
        list.add(new FileTemplate("Protocol#_name", ""));
    }
          
    public int getSize(){
        return list.size();        
    }
    
    public String getName(int i){        
        return list.get(i).getName();
    }
    
    public String getNotes(int i){
        return list.get(i).getNotes();
    }
}
