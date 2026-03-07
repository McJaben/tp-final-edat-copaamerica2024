/*
 * Código aportado por la cátedra en el Apunte 2, página 10.
 */

package estructuras.lineales;

/**
 * 
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 */
public class Nodo {
    private Object elem;
    private Nodo enlace;
    
    //Constructor
    public Nodo(Object elem, Nodo enlace){
        this.elem = elem;
        this.enlace = enlace;
    }
    
    //modificadoras
    public void setElem(Object elem){
        this.elem = elem;
    }
    
    public void setEnlace(Nodo enlace){
        this.enlace = enlace;
    }
    
    //Observadoras
    public Object getElem(){
        return elem;
    }
    
    public Nodo getEnlace(){
        return enlace;
    }
}
