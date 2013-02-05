/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tag;

import javacard.framework.*;

/**
 *
 * @author Simon
 */
public class Tag4 extends Applet {
    
    private final short TAG_SIZE = (short)0xFF;
    
    private byte[] CCfile = new byte[]{
        (byte)0x00, (byte)0x0F, // CCLEN 2 bytes 
        (byte)0x20,             // Mapping Version 1 byte
        (byte)0x00, (byte)0x3B, // MLe Max R-APDU
        (byte)0x00, (byte)0x34, // MLc Max C-APDU
        (byte)0x04,             // T field of the NDEF file control TLV
        (byte)0x06,             // L field of the NDEF file control TLV
                                // V field of the NDEF file control TLV 
        (byte)0xE1, (byte)0x04,     // File identifyer
        (byte)0x00, (byte)TAG_SIZE, // Max NDEF size
        (byte)0x00,                 // NDEF file read access condition
        (byte)0x00                  // NDEF file write access condition
    };
    
    private byte[] NDFfile = new byte[TAG_SIZE];
    
    private static final byte INS_READ_BINARY = (byte)0xB0;
    private static final byte INS_UPDATE_BINARY = (byte)0xD6;

    /**
     * Installs this applet.
     * 
     * @param bArray
     *            the array containing installation parameters
     * @param bOffset
     *            the starting offset in bArray
     * @param bLength
     *            the length in bytes of the parameter data in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Tag4();
    }

    /**
     * Only this class's install method should create the applet object.
     */
    protected Tag4() {
        register();
    }

    /**
     * Processes an incoming APDU.
     * 
     * @see APDU
     * @param apdu
     *            the incoming APDU
     */
    public void process(APDU apdu) { 
        if (selectingApplet()){
            return;
        }
        
        byte[] buffer = apdu.getBuffer();
        
        switch (buffer[ISO7816.OFFSET_INS]){
            case ISO7816.INS_SELECT:
                break;
                
            case INS_READ_BINARY:
                break;
            
            case INS_UPDATE_BINARY:
                break;
                
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
