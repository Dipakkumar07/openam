/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2015 ForgeRock AS.
 */

package org.forgerock.openam.selfservice.config.flows;

import static com.sun.identity.shared.datastruct.CollectionHelper.getBooleanMapAttrThrows;
import static com.sun.identity.shared.datastruct.CollectionHelper.getLocaleMapAttrThrows;
import static com.sun.identity.shared.datastruct.CollectionHelper.getLongMapAttrThrows;
import static com.sun.identity.shared.datastruct.CollectionHelper.getMapAttr;
import static com.sun.identity.shared.datastruct.CollectionHelper.getMapAttrThrows;
import static org.forgerock.openam.selfservice.config.flows.CommonSmsSelfServiceConstants.CAPTCHA_SECRET_KEY;
import static org.forgerock.openam.selfservice.config.flows.CommonSmsSelfServiceConstants.CAPTCHA_SITE_KEY;
import static org.forgerock.openam.selfservice.config.flows.CommonSmsSelfServiceConstants.CAPTCHA_VERIFICATION_URL;

import com.sun.identity.shared.datastruct.ValueNotFoundException;
import org.forgerock.openam.selfservice.config.ConsoleConfigExtractor;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * Extracts the forgotten username configuration instance from the console configuration attributes.
 *
 * @since 13.0.0
 */
public final class ForgottenUsernameExtractor implements ConsoleConfigExtractor<ForgottenUsernameConsoleConfig> {

    private final static String ENABLED_KEY = "forgerockRESTSecurityForgotUsernameEnabled";
    private final static String CAPTCHA_ENABLED_KEY = "forgerockRESTSecurityForgotUsernameCaptchaEnabled";
    private final static String KBA_ENABLED_KEY = "forgerockRESTSecurityForgotUsernameKbaEnabled";
    private final static String EMAIL_USERNAME_ENABLED_KEY = "forgerockRESTSecurityForgotUsernameEmailUsernameEnabled";
    private final static String SUBJECT_TRANSLATIONS_KEY = "forgerockRESTSecurityForgotUsernameEmailSubject";
    private final static String BODY_TRANSLATIONS_KEY = "forgerockRESTSecurityForgotUsernameEmailBody";
    private final static String SHOW_USERNAME_ENABLED_KEY = "forgerockRESTSecurityForgotUsernameShowUsernameEnabled";
    private final static String TOKEN_EXPIRY_KEY = "forgerockRESTSecurityForgotUsernameTokenTTL";
    private final static String SERVICE_CONFIG_CLASS_KEY = "forgerockRESTSecurityForgotUsernameServiceConfigClass";

    private final ConsoleConfigExtractor<KbaConsoleConfig> kbaExtractor;

    @Inject
    public ForgottenUsernameExtractor(ConsoleConfigExtractor<KbaConsoleConfig> kbaExtractor) {
        this.kbaExtractor = kbaExtractor;
    }

    @Override
    public ForgottenUsernameConsoleConfig extract(Map<String, Set<String>> consoleAttributes) {
        try {
            KbaConsoleConfig kbaConsoleConfig = kbaExtractor.extract(consoleAttributes);

            return ForgottenUsernameConsoleConfig
                    .newBuilder(consoleAttributes)
                    .setEnabled(getBooleanMapAttrThrows(consoleAttributes, ENABLED_KEY))
                    .setCaptchaEnabled(getBooleanMapAttrThrows(consoleAttributes, CAPTCHA_ENABLED_KEY))
                    .setSiteKey(getMapAttr(consoleAttributes, CAPTCHA_SITE_KEY))
                    .setSecretKey(getMapAttr(consoleAttributes, CAPTCHA_SECRET_KEY))
                    .setVerificationUrl(getMapAttr(consoleAttributes, CAPTCHA_VERIFICATION_URL))
                    .setKbaEnabled(getBooleanMapAttrThrows(consoleAttributes, KBA_ENABLED_KEY))
                    .setSecurityQuestions(kbaConsoleConfig.getSecurityQuestions())
                    .setMinimumAnswersToVerify(kbaConsoleConfig.getMinimumAnswersToVerify())
                    .setEmailEnabled(getBooleanMapAttrThrows(consoleAttributes, EMAIL_USERNAME_ENABLED_KEY))
                    .setSubjectTranslations(getLocaleMapAttrThrows(consoleAttributes, SUBJECT_TRANSLATIONS_KEY))
                    .setMessageTranslations(getLocaleMapAttrThrows(consoleAttributes, BODY_TRANSLATIONS_KEY))
                    .setShowUsernameEnabled(getBooleanMapAttrThrows(consoleAttributes, SHOW_USERNAME_ENABLED_KEY))
                    .setTokenExpiry(getLongMapAttrThrows(consoleAttributes, TOKEN_EXPIRY_KEY))
                    .setConfigProviderClass(getMapAttrThrows(consoleAttributes, SERVICE_CONFIG_CLASS_KEY))
                    .build();

        } catch (ValueNotFoundException e) {
            throw new IllegalArgumentException("Invalid console values", e);
        }
    }

}
