/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package namingtool;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Admin
 */
public class PatternTableModel extends AbstractTableModel{
    private ResultList r;    
    
    public PatternTableModel(ResultList r){
        super();
        this.r = r;                
    }
    
    @Override
    public int getRowCount(){
        return r.getSize();
    }
    
    @Override
    public int getColumnCount(){
        return 2; // There is no options
    }    
    
    
    @Override
    public String getColumnName(int col){
        if (0 == col){
            return "pattern";
        } else if (1 == col){
            return "notes";
        }
        return "";        
    }
                
    @Override
    public Object getValueAt(int pattern, int col){
        if (0 == col){            
            return r.getName(pattern);
        } else if (1 == col){
            return r.getNotes(pattern);
        }
        return "";
    }

    
}
