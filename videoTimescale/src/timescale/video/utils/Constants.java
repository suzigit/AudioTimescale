/*
 * Created on Nov 4, 2004
 */
package timescale.video.utils;

/**
 * @author Sergio Cavendish
 */
public abstract class Constants {

	/*
	 * Valores de start_codes
	 */
	// Start_Code_Prefix (hexa) = 00 00 01 
	public static byte[] START_CODE_PREFIX = { 0, 0, 1 };
	// Sequence_End_Code (hexa) = 00 00 01 b7
	public static byte[] SEQUENCE_END_CODE = { 0, 0, 1, -73 };
	// Extension_Start_Code (hexa) = 00 00 01 b5
	public static byte[] EXTENSION_START_CODE = { 0, 0, 1, -75 };
	// Group_Start_Code (hexa) = 00 00 01 b8
	public static byte[] GROUP_START_CODE = { 0, 0, 1, -72 };
	// Picture_Start_Code (hexa) = 00 00 01 00
	public static byte[] PICTURE_START_CODE = { 0, 0, 1, 0 };
	// User_Data_Start_Code (hexa) = 00 00 01 b2
	public static byte[] USER_DATA_START_CODE = { 0, 0, 1, -78 };

	// Códigos para o Extension_start_code_identifier

	public static final int SEQUENCE_EXTENSION_ID 									= 1;
	public static final int SEQUENCE_DISPLAY_EXTENSION_ID 					= 2;
	public static final int QUANT_MATRIX_EXTENSION_ID 							= 3;
	public static final int COPYRIGHT_EXTENSION_ID 									= 4;
	public static final int SEQUENCE_SCALABLE_EXTENSION_ID 					= 5;
	public static final int PICTURE_DISPLAY_EXTENSION_ID 						= 7;
	public static final int PICTURE_CODING_EXTENSION_ID 						= 8;
	public static final int PICTURE_SPATIAL_SCALABLE_EXTENSION_ID 	= 9;
	public static final int PICTURE_TEMPORAL_SCALABLE_EXTENSION_ID	= 10;
	public static final int CAMERA_PARAMETERS_EXTENSION_ID 					= 11;
	public static final int ITU_T_EXTENSION_ID 											= 12;

	/*
	 * Constantes para o tipo de figura.
	 */
	public final static int I_PIC = 1;
	public final static int P_PIC = 2;
	public final static int B_PIC = 3;
	public final static int UNKNOWN_PIC = 4;
	public final static int GOP = 5;
	public final static int SEQ = 6;

	/*
	 * Controle dos dados estatísticos
	 */
	public static final char INCREMENT = 1;
	public static final char DECREMENT = 0;
	public static final int PIC_WINDOW = 100;

	public static final int LAST_PICTURES = 0;
	public static final int WHOLE_STREAM = 1;
	public static final int LAST_SEQUENCE = 2;

	/*
	 * Tipo da estatística.
	 */
	public static final int ACTUAL = 0;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	
	/*
	 * PictureStructure.
	 */
	public static final int TOP_FIELD_PIC = 1;
	public static final int BOTTOM_FIELD_PIC = 2;
	public static final int FRAME_PIC = 3; 

}
