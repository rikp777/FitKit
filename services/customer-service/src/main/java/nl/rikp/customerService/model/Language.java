package nl.rikp.customerService.model;

import lombok.Getter;

/**
 * Enum representing supported languages.
 *
 * Each language is associated with a descriptive name.
 */
@Getter
public enum Language {
    EN("English"),
    US("English (US)"),
    NL("Dutch"),
    FR("French"),
    DE("German"),
    ES("Spanish"),
    IT("Italian"),
    PT("Portuguese"),
    RU("Russian"),
    DA("Danish"),
    SV("Swedish"),
    NO("Norwegian"),
    FI("Finnish"),
    IS("Icelandic"),
    TR("Turkish"),
    PL("Polish"),
    CS("Czech"),
    SK("Slovak"),
    HU("Hungarian"),
    RO("Romanian"),
    BG("Bulgarian"),
    EL("Greek"),
    HR("Croatian"),
    SR("Serbian"),
    SL("Slovenian"),
    LT("Lithuanian"),
    LV("Latvian"),
    ET("Estonian"),
    MT("Maltese"),
    GA("Irish"),
    CY("Welsh"),
    ZH("Chinese"),
    JA("Japanese");

    private final String languageName;

    Language(String languageName) {
        this.languageName = languageName;
    }

    /**
     * Converts a string representation of a language name to the corresponding Language enum.
     *
     * @param languageName the name of the language
     * @return the matching Language
     * @throws IllegalArgumentException if the languageName does not match any supported language
     */
    public static Language fromString(String languageName) {
        for (Language language : Language.values()) {
            if (language.languageName.equalsIgnoreCase(languageName)) {
                return language;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid language name: '%s'. Supported languages are: %s",
                        languageName,
                        String.join(", ", getSupportedLanguageNames()))
        );
    }

    /**
     * Converts a string representation of a language code to the corresponding Language enum.
     *
     * @param langCode the code of the language
     * @return the matching Language
     * @throws IllegalArgumentException if the langCode does not match any supported language code
     */
    public static Language fromStringLangCode(String langCode) {
        for (Language language : Language.values()) {
            if (language.name().equalsIgnoreCase(langCode)) {
                return language;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid language code: '%s'. Supported language codes are: %s",
                        langCode,
                        String.join(", ", getSupportedLanguageCodes()))
        );
    }

    /**
     * Gets the supported language names.
     *
     * @return an array of supported language names
     */
    private static String[] getSupportedLanguageNames() {
        return new String[] {
                EN.languageName, NL.languageName, FR.languageName, DE.languageName, ES.languageName,
                IT.languageName, PT.languageName, RU.languageName, DA.languageName, SV.languageName,
                NO.languageName, FI.languageName, IS.languageName, TR.languageName, PL.languageName,
                CS.languageName, SK.languageName, HU.languageName, RO.languageName, BG.languageName,
                EL.languageName, HR.languageName, SR.languageName, SL.languageName, LT.languageName,
                LV.languageName, ET.languageName, MT.languageName, GA.languageName, CY.languageName,
                ZH.languageName, JA.languageName
        };
    }

    /**
     * Gets the supported language codes.
     *
     * @return an array of supported language codes
     */
    private static String[] getSupportedLanguageCodes() {
        return new String[] {
                EN.name(), NL.name(), FR.name(), DE.name(), ES.name(),
                IT.name(), PT.name(), RU.name(), DA.name(), SV.name(),
                NO.name(), FI.name(), IS.name(), TR.name(), PL.name(),
                CS.name(), SK.name(), HU.name(), RO.name(), BG.name(),
                EL.name(), HR.name(), SR.name(), SL.name(), LT.name(),
                LV.name(), ET.name(), MT.name(), GA.name(), CY.name(),
                ZH.name(), JA.name()
        };
    }
}
