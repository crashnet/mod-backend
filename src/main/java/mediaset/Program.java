
package mediaset;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "label", "id", "url", "thumbnail", "mc" })
public class Program {

	// @JsonProperty("label")
	private String label;
	// @JsonProperty("id")
	private String id;
	// @JsonProperty("url")
	private String url;
	// @JsonProperty("thumbnail")
	private String thumbnail;
	// @JsonProperty("mc")
	private String mc;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String description;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	private Map<String, Section> sections;

	public Map<String, Section> getSections() {
		return sections;
	}

	public void setSections(Map<String, Section> sections) {
		this.sections = sections;
	}

	// @JsonProperty("label")
	public String getLabel() {
		return label;
	}

	// @JsonProperty("label")
	public void setLabel(String label) {
		this.label = label;
	}

	public Program withLabel(String label) {
		this.label = label;
		return this;
	}

	// @JsonProperty("id")
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Program [label=" + label + ", id=" + id + ", url=" + url + ", thumbnail=" + thumbnail + ", mc=" + mc
				+ ", additionalProperties=" + additionalProperties + "]";
	}

	// @JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public Program withId(String id) {
		this.id = id;
		return this;
	}

	// @JsonProperty("url")
	public String getUrl() {
		return url;
	}

	// @JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public Program withUrl(String url) {
		this.url = url;
		return this;
	}

	// @JsonProperty("thumbnail")
	public String getThumbnail() {
		return thumbnail.replace("http://", "https://");
	}

	// @JsonProperty("thumbnail")
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Program withThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
		return this;
	}

	// @JsonProperty("mc")
	public String getMc() {
		return mc;
	}

	// @JsonProperty("mc")
	public void setMc(String mc) {
		this.mc = mc;
	}

	public Program withMc(String mc) {
		this.mc = mc;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Program withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
