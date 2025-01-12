package org.apereo.cas.couchdb.gauth.credential;

import org.apereo.cas.authentication.OneTimeTokenAccount;
import org.apereo.cas.gauth.credential.GoogleAuthenticatorAccount;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.val;

import java.io.Serial;

/**
 * This is {@link CouchDbGoogleAuthenticatorAccount}.
 *
 * @author Timur Duehr
 * @since 6.0.0
 * @deprecated Since 7
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Deprecated(since = "7.0.0")
public class CouchDbGoogleAuthenticatorAccount extends GoogleAuthenticatorAccount {

    @Serial
    private static final long serialVersionUID = -4286976777933886751L;

    @JsonProperty("_id")
    private String cid;

    @JsonProperty("_rev")
    private String rev;

    /**
     * From one time token account into Couch db google authenticator account
     *
     * @param acct the acct
     * @return the couch db google authenticator account
     */
    public static CouchDbGoogleAuthenticatorAccount from(final OneTimeTokenAccount acct) {
        val account = CouchDbGoogleAuthenticatorAccount.builder()
            .id(acct.getId())
            .name(acct.getName())
            .username(acct.getUsername().trim().toLowerCase())
            .secretKey(acct.getSecretKey())
            .validationCode(acct.getValidationCode())
            .scratchCodes(acct.getScratchCodes())
            .registrationDate(acct.getRegistrationDate())
            .build();

        if (acct instanceof CouchDbGoogleAuthenticatorAccount gAcct) {
            return account.setCid(gAcct.getCid()).setRev(gAcct.getRev());
        }
        return account;
    }
}
