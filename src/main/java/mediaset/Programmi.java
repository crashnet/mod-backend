
package mediaset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "group" })
public class Programmi {

	@JsonProperty("group")
	private List<Group> group = new ArrayList<Group>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("group")
	public List<Group> getGroup() {
		return group;
	}

	@JsonProperty("group")
	public void setGroup(List<Group> group) {
		this.group = group;
	}

	public Programmi withGroup(List<Group> group) {
		this.group = group;
		return this;
	}

	@Override
	public String toString() {
		return "Programmi [group=" + group + ", additionalProperties=" + additionalProperties + "]";
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Programmi withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
