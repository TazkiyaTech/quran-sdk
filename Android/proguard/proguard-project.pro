-keepparameternames

-keep public interface com.thinkincode.quran.sdk.** {
    <methods>;
}

-keep public class com.thinkincode.quran.sdk.** {
    public <init>(...);
    public <fields>;
    public static <fields>;
    public <methods>;
    public static <methods>;
}

-keep public enum com.thinkincode.quran.sdk.** {
    public <init>(...);
    public <fields>;
    public static <fields>;
    public <methods>;
    public static <methods>;
}