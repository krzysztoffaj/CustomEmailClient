package com.app.emailcomposer;

import com.app.common.Email;

public interface EmailComposerModel {
    void sendEmail(Email email);

    void saveDraft(Email email);
}
