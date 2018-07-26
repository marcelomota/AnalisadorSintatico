package models;

import java.util.ArrayList;

public class TabelaVarConst {
    
    private ArrayList<NoVarConst> varConst;

    public TabelaVarConst() {
        this.varConst = new ArrayList();
    }

    public ArrayList<NoVarConst> getVarConst() {
        return varConst;
    }

    public void addVarConst(NoVarConst noVarConst) {
        this.varConst.add(noVarConst);
    }
    
}
