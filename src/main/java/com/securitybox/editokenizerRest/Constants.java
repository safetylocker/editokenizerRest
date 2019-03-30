package com.securitybox.editokenizerRest;

public class Constants {
    static final String requestTokenizerEDISample  ="UNA:+. ?'UNB+UNOC:3+SENDERID+RECIPIENTID+100615:0100+1006150100000++MYCOMPANY'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Sender AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@Sender.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'UNT+XX+1'UNZ+1+1006150100000'";
    static final String requestDeTokenizerEDISample="UNA:+. ?'UNB+UNOC:3+SENDERID+RECIPIENTID+100615:0100+1006150100000++MYCOMPANY'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Sender AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@Sender.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'UNT+YY+1'UNZ+1+1006150100000'";
    static final String elementsToTokenizeJsonEDIFACT ="[{\"segmentNumber\":10, \"dataElementNumber\":5, \"dataElementPosition\":1, \"dataElementLength\":30},{\"segmentNumber\":16, \"dataElementNumber\":3, \"dataElementPosition\":2, \"dataElementLength\":20}]";
    static final String elementsToDeTokenizeJsonEDIFACT ="[{\"segmentNumber\":10, \"dataElementNumber\":5, \"dataElementPosition\":1},{\"segmentNumber\":16, \"dataElementNumber\":3, \"dataElementPosition\":2}]";

    static final String requestTokenizerCSVSample  ="Raw1_column_1_data:Raw1_column_2_data:Raw1_column_3_data:Raw1_column_4_data\nRaw2_column_1_data:Raw2_column_2_data:Raw2_column_3_data:Raw2_column_4_data";
    static final String requestDeTokenizerCSVSample  ="Raw1_column_1_data:Raw1_column_2_data:Raw1_column_3_data:Raw1_column_4_data\nRaw2_column_1_data:Raw2_column_2_data:Raw2_column_3_data:Raw2_column_4_data";
    static final String elementsToTokenizeJsonCSV ="[{\"dataElementPosition\":1, \"dataElementLength\":30},{\"dataElementPosition\":2, \"dataElementLength\":20}]";
    static final String elementsToDeTokenizeJsonExampleCSV="[{\"dataElementPosition\":1, \"dataElementLength\":30},{\"dataElementPosition\":2, \"dataElementLength\":20}]";

}
