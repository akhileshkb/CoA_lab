package processor.pipeline;
import processor.pipeline.ControlUnit;

public class ArithmeticLogicUnit {

    int a;
    int b;
    ControlUnit controlUnit;
    int INT_MAX;

    public ArithmeticLogicUnit() {
        INT_MAX = 2147483647;
    }
    
    public ControlUnit getControlUnit() {
        return this.controlUnit;
    }

    public void setControlUnit(ControlUnit controlUnit) {
        // System.out.println("HI00");
        this.controlUnit = controlUnit;
    }

    public int getA() {
        return this.a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return this.b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getALUResult(){

        if(controlUnit.isLoad() || controlUnit.isStore()) {  // As [rs1+imm] is needed in Ld/St
            return a + b;
        }

        if(controlUnit.isAdd()){
            long temp = a + b;
            if(temp - INT_MAX > 0) {
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return a + b;
        }

        if(controlUnit.isSub()){
            return (this.a - this.b);
        }

        if(controlUnit.isMul()){
            long temp = a * b;
            if(temp - INT_MAX > 0) {
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return a * b;
        }

        if(controlUnit.isDiv()){
            return a / b;
        }

        if(controlUnit.isAnd()){
            return a & b;
        }

        if(controlUnit.isOr()){
            return a | b;
        }

        if(controlUnit.isXor()){
            return a ^ b;
        }

        if(controlUnit.isSlt()){
            return (a < b) ? 1 : 0;
        }

        if(controlUnit.isSll()){
            long temp = (a << b);
            if(temp - INT_MAX > 0){
                controlUnit.setIsOverflow(true);
                
                String binStr = Long.toString(temp);
                String overflowStr = binStr.substring(0, 32);
                String resultStr = binStr.substring(32);

                controlUnit.setOverflow(Integer.parseInt(overflowStr));
                return Integer.parseInt(resultStr);
            }

            return (a << b);
        }

        if(controlUnit.isSrl()){
            return (a >> b);
        }

        if(controlUnit.isSra()){
            return (b >>> b);
        }
        return 0;
    }

    public int getMod(){
        return a % b;
    }

    public boolean getFlag(String type){
        switch(type){
            case "E": return a == b;
            case "GT": return a > b;
            case "LT": return (a < b);
            case "NE": return a != b;
        }
        return false;
    }

}