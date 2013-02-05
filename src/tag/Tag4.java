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
    
    private byte[] NDEFfile = new byte[TAG_SIZE];
    
    private static final byte INS_READ_BINARY = (byte)0xB0;
    private static final byte INS_UPDATE_BINARY = (byte)0xD6;
    
    private static final byte PAR_SELECT_BY_ID = (byte)0x00;
    private static final byte PAR_FIRST_ONLY_OCC = (byte)0x0C;
    
    private static final short CC_ID = (short)0xE103;
    private static final short NDEF_ID = (short)0xE104;
    
    private static final short STATE_WAIT = 1;
    private static final short STATE_CC_SELECTED = 2; 
    private static final short STATE_NDEF_SELECTED = 3;
    
    private short currentState = STATE_WAIT;

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
            currentState = STATE_WAIT;
            return;
        }
        
        byte[] buffer = apdu.getBuffer();
        
        switch (buffer[ISO7816.OFFSET_INS]){
            case ISO7816.INS_SELECT:
                if (buffer[ISO7816.OFFSET_P1] == PAR_SELECT_BY_ID){
                   if (buffer[ISO7816.OFFSET_P2] == PAR_FIRST_ONLY_OCC){
                       short data = javacard.framework.Util.makeShort(
                               buffer[ISO7816.OFFSET_CDATA], 
                               buffer[ISO7816.OFFSET_CDATA+1]);
                       
                       if (data == CC_ID){
                           currentState = STATE_CC_SELECTED;
                       }
                       else if (data == NDEF_ID){
                           currentState = STATE_NDEF_SELECTED;
                       } else {
                           ISOException.throwIt(ISO7816.SW_DATA_INVALID);
                       }
                   } else {
                       ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
                   }
                } else {
                    ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
                }
                break;
    
            case INS_READ_BINARY:
                short offset = javacard.framework.Util.makeShort(
                            buffer[ISO7816.OFFSET_P1], 
                            buffer[ISO7816.OFFSET_P2]);
                short lenght = buffer[ISO7816.OFFSET_LC];
                
                apdu.setOutgoing();
                
                if (currentState == STATE_WAIT){
                    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
                }
                else if (currentState == STATE_CC_SELECTED){
                    if (offset+lenght > 0x0f){
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                    javacard.framework.Util.arrayCopy(
                            CCfile, offset, buffer, (short)0, lenght);
                    apdu.setOutgoingLength(lenght);
                    apdu.sendBytes((short)0, lenght); 
                }
                else if (currentState == STATE_NDEF_SELECTED){
                    if (offset + lenght > 0xff){
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                    javacard.framework.Util.arrayCopy(
                            NDEFfile, offset, buffer, (short)0, lenght);
                    apdu.setOutgoingLength(lenght);
                    apdu.sendBytes((short)0, lenght); 
                }
                break;
            
            case INS_UPDATE_BINARY:
                break;
                
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
