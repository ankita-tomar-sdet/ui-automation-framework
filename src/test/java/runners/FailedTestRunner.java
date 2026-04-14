package runners;

import io.cucumber.junit.platform.engine.Cucumber;

@Cucumber
// This runner points specifically to the rerun.txt file created by the main runner
public class FailedTestRunner {
    // This runner only executes the specific scenarios listed in target/rerun.txt
}