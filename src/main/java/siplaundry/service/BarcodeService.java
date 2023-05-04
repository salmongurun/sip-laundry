package siplaundry.service;

// import com.google.zxing.oned.EAN13Writer;

public class BarcodeService {

    public String setIdBarcode(String date, int id){
        String newId = Integer.toString(id);
        return date + "-" + newId;
    }

    // private void generate(String IdBarcode){
        
    // }
}
