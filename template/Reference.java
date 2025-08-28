package $package;

/**
 * Tags storage class, you can change at will
 */
public class Reference {

    public static final String MOD_ID = "$mod_id";
    public static final String MOD_NAME = "$mod_name";
    public static final String VERSION = "$mod_version";
    public static final String BRAND = MOD_ID + " " + VERSION;

    public static String getVersion() {
        return VERSION;
    }

    public static String getBrand() {
        return BRAND;
    }

}
