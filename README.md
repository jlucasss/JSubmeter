# JSubmeter

JSubmeter is a simple application that tests a solution class for a given file with text input.

Note: This project is in an early version, although it works, there may be a better way to do it.

## Installation

Donwload [.jar](https://) file and run in terminal:

```bash
> java -jar 'JSubmeter.jar'

JSubmeter v0.01

java -jar 'JSubmeter.jar' <OutputPath> <SolutionPath> <InputPath> [options]
 <OutputPath>: Path used to create temporary files and output(Ex:. 'C://').
 <SolutionPath>: Path of the .java file(Ex:. 'C://Main.java').
 <InputPath>: Path of the file containing the input(Ex:. 'C://input.txt').
 [options]: working specifications.

Can be [options]:
 --maintainTemporaries: do not delete files created for compilation and execution.
 --noSaveRuntime: do not save the runtime on output.
 --noSaveOutput: do not save console output.
Note: If all '--noSave...' arguments are used, nothing will be saved.

```

or like libary in your project.

## Usage

At the terminal:

```bash
JSubmeter v0.01

java -jar 'JSubmit.jar' "C:/output/" "C:/Solution.java" "C:/input.txt" 

```

In another project:

```java
		DataPerson data = new DataPerson(pathSolutionFile, 
							pathInput,
							pathOutput);

		Submeter submeter = new Submeter(data);

		try {

			submeter.submit();

		} catch(Exception e) {
			e.printStackTrace();
		}

		submeter.saveOutputCurrent();

		System.out.println(data.toString());
```

Or for more than one test for the same solution file:

```java
		submeter.preSubmit();
			
		submeter.finishSubmit("input1.in");
		System.out.println(data.toString());

		submeter.finishSubmit("input2.in");
		System.out.println(data.toString());
```

Note: Data can also be accessed via the `DataPerson` class.

Other example files:

`Solution.java`:

```java
import java.util.Scanner;

public class Solution {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int n = in.nextInt();

		System.out.println(n*2);

	}

}
```
Note: the input must be `System.in` and output must be `System.out.print...` .

`Solution.java.txt`(output):

```
100
10
```

First line is the runtime and second is the output.

## Roadmap

- Add support for new programming languages;
- Add support for more input files to a solution file
- Create more test cases;

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)