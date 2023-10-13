package todo.microservice;

import java.io.IOException;
import java.util.List;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.Serializer;

/**
 * Custom serializer for {@link DefaultPage} as a workaround for https://github.com/micronaut-projects/micronaut-serialization/issues/307.
 *
 * Backport from https://github.com/micronaut-projects/micronaut-data/commit/1190f94e0286f773d21e805531f61cb23a7e18fa.
 *
 * @author Denis Stepanov
 * @since 4.0.0
 */
@Prototype
final class DefaultPageSerializer implements Serializer<Page<Object>> {

    @SuppressWarnings("unchecked")
	@Override
    public void serialize(Encoder encoder, EncoderContext context, Argument<? extends Page<Object>> type, Page<Object> page) throws IOException {
        Encoder e = encoder.encodeObject(type);

        e.encodeKey("content");
        Argument<List<Object>> contentType = Argument.listOf((Argument<Object>) type.getFirstTypeVariable().orElse(Argument.OBJECT_ARGUMENT));
        context.findSerializer(contentType)
            .createSpecific(context, contentType)
            .serialize(e, context, contentType, page.getContent());

        e.encodeKey("pageable");
        Argument<Pageable> pageable = Argument.of(Pageable.class);
        context.findSerializer(pageable)
            .createSpecific(context, pageable)
            .serialize(e, context, pageable, page.getPageable());

        e.encodeKey("totalSize");
        e.encodeLong(page.getTotalSize());

        e.finishStructure();
    }
}