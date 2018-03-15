package com.example.unit.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.example.api.core.repository.model.User;
import com.example.api.resources.user.request.UserRequest;
import com.example.api.resources.user.response.UserResponse;

public class DemoTemplateLoader implements TemplateLoader {

    public static final String USER_CREATE_REQUEST = "user-create-request";
    public static final String USER_CREATE_RESPONSE = "user-create-response";
    public static final String USER = "user-entity-no-id";
    public static final String USER_WITH_ID = "user-entity";

    @Override
    public void load() {

        Fixture.of(UserRequest.class).addTemplate(USER_CREATE_REQUEST, new Rule() {{
            add("slug", "user-slug");
            add("name", "user-name");
        }});

        Fixture.of(UserResponse.class).addTemplate(USER_CREATE_RESPONSE, new Rule() {{
            add("slug", "user-slug");
            add("name", "user-name");
        }});

        Fixture.of(User.class).addTemplate(USER, new Rule() {{
            add("slug", "user-slug");
            add("name", "user-name");
        }});

        Fixture.of(User.class).addTemplate(USER_WITH_ID, new Rule() {{
            add("objRef", "i12i3jo1ij23");
            add("slug", "user-slug");
            add("name", "user-name");
        }});


    }
}
