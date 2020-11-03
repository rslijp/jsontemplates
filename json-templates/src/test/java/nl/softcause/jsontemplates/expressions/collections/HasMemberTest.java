package nl.softcause.jsontemplates.expressions.collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import nl.softcause.jsontemplates.expressions.Constant;
import nl.softcause.jsontemplates.expressions.IExpression;
import nl.softcause.jsontemplates.expressions.TestModel;
import nl.softcause.jsontemplates.expressions.Variable;
import org.junit.Test;

public class HasMemberTest {

    @Test
    public void should_return_true_for_value_compare() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new int[] {1, 2, 3}), new Constant(2)));

        var r = hasMember.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_return_true_for_reference_compare() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new String[] {"a", "b", "c"}), new Constant("b")));

        var r = hasMember.evaluate(null);

        assertThat(r, is(true));
    }


    @Test
    public void should_return_false_for_missing_entry_of_value_compare() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new int[] {1, 2, 3}), new Constant(4)));

        var r = hasMember.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_return_false_for_missing_entry_of_reference_compare() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new String[] {"a", "b", "c"}), new Constant("d")));

        var r = hasMember.evaluate(null);

        assertThat(r, is(false));
    }


    @Test
    public void should_be_null_safe_1st() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(null), new Constant("a")));

        var r = hasMember.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_be_null_safe_2nd() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new String[] {"a", "b", "c"}), new Constant(null)));

        var r = hasMember.evaluate(null);

        assertThat(r, is(false));
    }

    @Test
    public void should_match_on_null_value() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Constant(new String[] {"a", "b", "c", null}), new Constant(null)));

        var r = hasMember.evaluate(null);

        assertThat(r, is(true));
    }

    @Test
    public void should_consume_variables() {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Variable("L"), new Variable("R")));
        var model = new TestModel().put("L", new int[] {1, 2, 3}).put("R", 2);

        var r = hasMember.evaluate(model);

        assertThat(r, is(true));
    }

    @Test
    public void should_serialize_to_json() throws IOException {
        var hasMember = new HasMember();
        hasMember.setArguments(Arrays.asList(new Variable("V"), new Constant("a")));

        var json = new ObjectMapper().writeValueAsString(hasMember);

        var obj = new ObjectMapper().readValue(json, IExpression.class);

        assertThat(hasMember, is(obj));
    }
}
