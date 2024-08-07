package com.zebra.scannercontrol.app.barcode;

/**
 * Helper class to create barcodes
 */
public class BarcodeTypes {
    public static final int   ST_NOT_APP			= 0x00;
    public static final int   ST_CODE_39			= 0x01;
    public static final int   ST_CODABAR			= 0x02;
    public static final int   ST_CODE_128			= 0x03;
    public static final int   ST_D2OF5				= 0x04;
    public static final int   ST_IATA				= 0x05;
    public static final int   ST_I2OF5				= 0x06;
    public static final int   ST_CODE93				= 0x07;
    public static final int   ST_UPCA				= 0x08;
    public static final int   ST_UPCE0				= 0x09;
    public static final int   ST_EAN8				= 0x0a;
    public static final int   ST_EAN13				= 0x0b;
    public static final int   ST_CODE11				= 0x0c;
    public static final int   ST_CODE49				= 0x0d;
    public static final int   ST_MSI				= 0x0e;
    public static final int   ST_EAN128				= 0x0f;
    public static final int   ST_UPCE1				= 0x10;
    public static final int   ST_PDF417				= 0x11;
    public static final int   ST_CODE16K			= 0x12;
    public static final int   ST_C39FULL			= 0x13;
    public static final int   ST_UPCD				= 0x14;
    public static final int   ST_TRIOPTIC			= 0x15;
    public static final int   ST_BOOKLAND			= 0x16;
    public static final int   ST_COUPON				= 0x17;
    public static final int   ST_NW7				= 0x18;
    public static final int   ST_ISBT128			= 0x19;
    public static final int   ST_MICRO_PDF			= 0x1a;
    public static final int   ST_DATAMATRIX			= 0x1b;
    public static final int   ST_QR_CODE			= 0x1c;
    public static final int   ST_MICRO_PDF_CCA		= 0x1d;
    public static final int   ST_POSTNET_US			= 0x1e;
    public static final int   ST_PLANET_CODE		= 0x1f;
    public static final int   ST_CODE_32			= 0x20;
    public static final int   ST_ISBT128_CON		= 0x21;
    public static final int   ST_JAPAN_POSTAL		= 0x22;
    public static final int   ST_AUS_POSTAL			= 0x23;
    public static final int   ST_DUTCH_POSTAL		= 0x24;
    public static final int   ST_MAXICODE			= 0x25;
    public static final int   ST_CANADIN_POSTAL		= 0x26;
    public static final int   ST_UK_POSTAL			= 0x27;
    public static final int   ST_MACRO_PDF			= 0x28;
    public static final int   ST_MACRO_QR_CODE      = 0x29;
    public static final int   ST_MICRO_QR_CODE		= 0x2c;
    public static final int	  ST_AZTEC				= 0x2d;
    public static final int   ST_AZTEC_RUNE_CODE    = 0x2E;
    public static final int   ST_FRENCH_LOTTERY     = 0x2F;
    public static final int   ST_RSS14				= 0x30;
    public static final int   ST_RSS_LIMITET		= 0x31;
    public static final int   ST_RSS_EXPANDED		= 0x32;
    public static final int   ST_PARAMETER_FNC3	    = 0x33;
    public static final int   ST_4STATE_US	        = 0x34;
    public static final int   ST_4STATE_US4	        = 0x35;
    public static final int   ST_ISSN			    = 0x36;
    public static final int   ST_SCANLET			= 0x37;
    public static final int   ST_CUE_CAT_CODE	    = 0x38;
    public static final int   ST_MATRIX_2_OF_5	    = 0x39;
    public static final int   ST_UPCA_2				= 0x48;
    public static final int   ST_UPCE0_2			= 0x49;
    public static final int   ST_EAN8_2				= 0x4a;
    public static final int   ST_EAN13_2			= 0x4b;
    public static final int   ST_UPCE1_2			= 0x50;
    public static final int   ST_CCA_EAN128			= 0x51;
    public static final int   ST_CCA_EAN13			= 0x52;
    public static final int   ST_CCA_EAN8			= 0x53;
    public static final int   ST_CCA_RSS_EXPANDED	= 0x54;
    public static final int   ST_CCA_RSS_LIMITED	= 0x55;
    public static final int   ST_CCA_RSS14			= 0x56;
    public static final int   ST_CCA_UPCA			= 0x57;
    public static final int   ST_CCA_UPCE			= 0x58;
    public static final int   ST_CCC_EAN128			= 0x59;
    public static final int   ST_TLC39				= 0x5A;
    public static final int   ST_CCB_EAN128			= 0x61;
    public static final int   ST_CCB_EAN13			= 0x62;
    public static final int   ST_CCB_EAN8			= 0x63;
    public static final int   ST_CCB_RSS_EXPANDED	= 0x64;
    public static final int   ST_CCB_RSS_LIMITED	= 0x65;
    public static final int   ST_CCB_RSS14			= 0x66;
    public static final int   ST_CCB_UPCA			= 0x67;
    public static final int   ST_CCB_UPCE			= 0x68;
    public static final int   ST_SIGNATURE_CAPTURE	= 0x69;
    public static final int   ST_MATRIX2OF5_OLD		= 0x71;
    public static final int   ST_CHINESE2OF5		= 0x72;
    public static final int   ST_KOREAN2OF5		    = 0x73;
    public static final int   ST_DATAMATRIX_PARAM   = 0x74;
    public static final int   ST_CODEZ		        = 0x75;
    public static final int   ST_UPCA_5				= 0x88;
    public static final int   ST_UPCE0_5			= 0x89;
    public static final int   ST_MULTI_BARCODE_SSI	= 0x98;
    public static final int   ST_EAN8_5				= 0x8a;
    public static final int   ST_EAN13_5			= 0x8b;
    public static final int   ST_UPCE1_5			= 0x90;
    public static final int   ST_MACRO_MICRO_PDF	= 0x9A;
    public static final int   ST_OCRB	            = 0xA0;
    public static final int   ST_NEW_COUPON	        = 0xB4;
    public static final int   ST_HAN_XIN	        = 0xB7;
    public static final int   ST_GS1_DATAMATRIX     = 0xC1;
    public static final int   ST_DOT_CODE           = 0xC4;
    public static final int   ST_GRID_MATRIX        = 0xC8;
    public static final int   ST_RFID_RAW	        = 0xE0;
    public static final int   ST_RFID_URI	        = 0xE1;

    public static String getBarcodeTypeName(int barcodeType){
        switch (barcodeType){
            case ST_NOT_APP: return "Unknown,";
            case ST_CODE_39: return "Code 39,";
            case ST_CODABAR: return "Codabar,";
            case ST_CODE_128: return "Code 128,";
            case ST_D2OF5: return "Discrete 2 of 5,";
            case ST_IATA: return "IATA,";
            case ST_I2OF5: return "Interleaved 2 of 5,";
            case ST_CODE93: return "Code 93,,";
            case ST_UPCA: return "UPCA,";
            case ST_UPCE0: return "UPCE 0,";
            case ST_EAN8: return "EAN 8,";
            case ST_EAN13: return "EAN 13,";
            case ST_CODE11: return "Code 11,";
            case ST_CODE49: return "Code 49,";
            case ST_MSI: return "MSI,";
            case ST_EAN128: return "EAN 128,";
            case ST_UPCE1: return "UPCE 1,";
            case ST_PDF417: return "PDF 417,";
            case ST_CODE16K: return "Code 16K,";
            case ST_C39FULL: return "Code 39 Full ASCII,";
            case ST_UPCD: return "UPCD,";
            case ST_TRIOPTIC: return "Trioptic,";
            case ST_BOOKLAND: return "Bookland,";
            case ST_COUPON: return "Coupon Code,";
            case ST_NW7: return "NW7,";
            case ST_ISBT128: return "ISBT-128,";
            case ST_MICRO_PDF: return "Micro PDF,";
            case ST_DATAMATRIX: return "Data Matrix,";
            case ST_QR_CODE: return "QR Code,";
            case ST_MICRO_PDF_CCA: return "Micro PDF CCA,";
            case ST_POSTNET_US: return "Postnet US,";
            case ST_PLANET_CODE: return "Planet Code,";
            case ST_CODE_32: return "Code 32,";
            case ST_ISBT128_CON: return "ISBT-128 Concat,";
            case ST_JAPAN_POSTAL: return "Japan Postal,";
            case ST_AUS_POSTAL: return "Aus Postal,";
            case ST_DUTCH_POSTAL: return "Dutch Postal,";
            case ST_MAXICODE: return "Maxicode,";
            case ST_CANADIN_POSTAL: return "Canada Postal,";
            case ST_UK_POSTAL: return "UK Postal,";
            case ST_MACRO_PDF: return "Macro PDF-417,";
            case ST_MACRO_QR_CODE: return "Macro QR Code,";
            case ST_RSS14: return "GS1 Databar,";
            case ST_RSS_LIMITET: return "GS1 Databar Limited,";
            case ST_RSS_EXPANDED: return "GS1 Databar Expanded,";
            case ST_SCANLET: return "Scanlet Webcode,";
            case ST_UPCA_2: return "UPCA + 2,";
            case ST_UPCE0_2: return "UPCE0 + 2,";
            case ST_EAN8_2: return "EAN8 + 2,";
            case ST_EAN13_2: return "EAN13 + 2,";
            case ST_UPCE1_2: return "UPCE1 + 2,";
            case ST_CCA_EAN128: return "CC-A + EAN-128,";
            case ST_CCA_EAN13: return "CC-A + EAN-13,";
            case ST_CCA_EAN8: return "CC-A + EAN-8,";
            case ST_CCA_RSS_EXPANDED: return "CC-A + GS1 Databar Expanded,";
            case ST_CCA_RSS_LIMITED: return "CC-A + GS1 Databar Limited,";
            case ST_CCA_RSS14: return "CC-A + GS1 Databar,";
            case ST_CCA_UPCA: return "CC-A + UPCA,";
            case ST_CCA_UPCE: return "CC-A + UPC-E,";
            case ST_CCC_EAN128: return "CC-C + EAN-128,";
            case ST_TLC39: return "TLC-39,";
            case ST_CCB_EAN128: return "CC-B + EAN-128,";
            case ST_CCB_EAN13: return "CC-B + EAN-13,";
            case ST_CCB_EAN8: return "CC-B + EAN-8,";
            case ST_CCB_RSS_EXPANDED: return "CC-B + GS1 Databar Expanded,";
            case ST_CCB_RSS_LIMITED: return "CC-B + GS1 Databar Limited,";
            case ST_CCB_RSS14: return "CC-B + GS1 Databar,";
            case ST_CCB_UPCA: return "CC-B + UPC-A,";
            case ST_CCB_UPCE: return "CC-B + UPC-E,";
            case ST_SIGNATURE_CAPTURE: return "Signature,";
            case ST_MATRIX2OF5_OLD: return "Matrix 2 Of 5,";
            case ST_MATRIX_2_OF_5: return "Matrix 2 Of 5,";
            case ST_CHINESE2OF5: return "Chinese 2 Of 5,";
            case ST_UPCA_5: return "UPCA 5,";
            case ST_UPCE0_5: return "UPCE0 5,";
            case ST_MULTI_BARCODE_SSI: return "Multi Barcode SSI,";
            case ST_EAN8_5: return "EAN8 5,";
            case ST_EAN13_5: return "EAN13 5,";
            case ST_UPCE1_5: return "UPCE1 5,";
            case ST_MACRO_MICRO_PDF: return "Macro Micro PDF,";
            case ST_MICRO_QR_CODE: return "Micro QR Code,";
            case ST_AZTEC: return "Aztec Code,";
            case ST_AZTEC_RUNE_CODE: return "Aztec Rune Code,";
            case ST_FRENCH_LOTTERY: return "French Lottery,";
            case ST_PARAMETER_FNC3: return "Parameter (FNC3),";
            case ST_4STATE_US: return "4 State US,";
            case ST_4STATE_US4: return "4 State US4,";
            case ST_CUE_CAT_CODE: return "Cue CAT Code,";
            case ST_KOREAN2OF5: return "Korean 3 Of 5,";
            case ST_DATAMATRIX_PARAM: return "Datamatrix Parameter,";
            case ST_CODEZ: return "Code Z,";
            case ST_OCRB	: return "OCRB,";
            case ST_RFID_RAW	: return "RFID Raw,";
            case ST_RFID_URI: return "RFID URI,";
            case ST_ISSN:return "ISSN,";
            case ST_HAN_XIN:return "Han Xin,";
            case ST_NEW_COUPON:return "GS1 Databar Expanded Coupon,";
            case ST_GS1_DATAMATRIX: return "GS1 Datamatrix,";
            case ST_DOT_CODE: return "DotCode,";
            case ST_GRID_MATRIX: return "Grid Matrix,";
            default: return "";
        }
    }
}
