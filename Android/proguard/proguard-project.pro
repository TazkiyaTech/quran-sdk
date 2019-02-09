-keepparameternames

-keep public interface com.tazkiyatech.quran.sdk.** {
    <methods>;
}

-keep public class com.tazkiyatech.quran.sdk.** {
    public <init>(...);
    public <fields>;
    public static <fields>;
    public <methods>;
    public static <methods>;
}

-keep public enum com.tazkiyatech.quran.sdk.** {
    public <init>(...);
    public <fields>;
    public static <fields>;
    public <methods>;
    public static <methods>;
}