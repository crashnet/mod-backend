
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
@JsonPropertyOrder({ "index", "program" })
public class Group {

	@JsonProperty("index")
	private String index;
	@JsonProperty("program")
	private List<Program> program = new ArrayList<Program>();

	@Override
	public String toString() {
		return "Group [index=" + index + ", program=" + program + ", additionalProperties=" + additionalProperties
				+ "]";
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("index")
	public String getIndex() {
		return index;
	}

	@JsonProperty("index")
	public void setIndex(String index) {
		this.index = index;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public Group withIndex(String index) {
		this.index = index;
		return this;
	}

	@JsonProperty("program")
	public List<Program> getProgram() {
		return program;
	}

	@JsonProperty("program")
	public void setProgram(List<Program> program) {
		this.program = program;
	}

	public Group withProgram(List<Program> program) {
		this.program = program;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Group withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
