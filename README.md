# JSubmeter

JSubmeter is a simple library that tests a solution class for a given file with text input.

Note: This project is in an early version, although it works, there may be a better way to do it.

## Installation

Download [.jar](https://github.com/jlucasss/JSubmeter/releases/tag/v0.0.3) file and run in terminal:

```bash
> java -jar jsubmeter-0.0.3.jar

JSubmeter v0.0.3

java -jar jsubmeter-0.0.3.jar <SolutionFile> <InputFolder> <InputFileName> <OutputFolder> [options]
 <SolutionFile>: Path of the .java file(Ex:. 'C://Main.java').
 <InputFolder>: Path of the file containing the input(Ex:. 'C://input.txt').
 <InputFileName>: File name with extension(if contains) of input(Ex:. 'input.txt').
 <OutputFolder>: Path used to create temporary files and output(Ex:. 'C://').
 [options]: working specifications.

Can be [options]:
 --maintainTemporaries: do not delete files created for compilation and execution.[default: true]
 --noSaveRuntime: do not save the runtime on output.[default: true]
 --noSaveOutput: do not save console output.[default: true]
 --printNewSourceCode: prints new source code (if the current one has been modified).[default: false]
Note: If all "--noSave..." arguments are used, nothing will be saved.
```

or like libary in your project.

## Usage

At the terminal:

```bash

java -jar jsubmeter-0.0.3.jar "C:/Solution.java" "C:/" "input.txt" "C:/output/" 

```

In another project:

```java
		DataPerson data = new DataPerson(pathSolutionFile, 
							pathInputFolder,
							fileInputName, // or only ""
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
