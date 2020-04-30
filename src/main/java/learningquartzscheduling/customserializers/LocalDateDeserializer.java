package learningquartzscheduling.customserializers;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
	private static final long serialVersionUID = 1L;

	protected LocalDateDeserializer() {
		super(LocalDate.class);
	}

	@Override
	public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String readValueAs = jp.readValueAs(String.class);
		int indexOf = readValueAs.indexOf("T");
		if (indexOf != -1) {
			readValueAs = readValueAs.substring(0, indexOf);
		}
		return LocalDate.parse(readValueAs);
	}
}