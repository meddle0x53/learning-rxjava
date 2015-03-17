package com.packtpub.reactive.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.observables.GroupedObservable;

import com.packtpub.reactive.chapter01.ObservableVSIterator;
import com.packtpub.reactive.chapter01.ReactiveSumV1;
import com.packtpub.reactive.chapter02.FunctionsExample;
import com.packtpub.reactive.chapter02.Java8LambdasSyntaxIntroduction;
import com.packtpub.reactive.chapter02.MonadsExample;
import com.packtpub.reactive.chapter02.ObservablesAndMonads;
import com.packtpub.reactive.chapter02.ReactiveSumWithLambdas;
import com.packtpub.reactive.eight.ComposeExample;
import com.packtpub.reactive.eight.LiftExample;
import com.packtpub.reactive.eight.ResourceManagementExample;
import com.packtpub.reactive.five.CombiningObservablesExample;
import com.packtpub.reactive.five.ConditionalsExample;
import com.packtpub.reactive.five.HandlingErrorsExamples;
import com.packtpub.reactive.five.HttpRequestsExample;
import com.packtpub.reactive.four.FilteringExamples;
import com.packtpub.reactive.four.FlatMapAndFiles;
import com.packtpub.reactive.four.GroupingExamples;
import com.packtpub.reactive.four.MappingExamples;
import com.packtpub.reactive.four.OtherTransformationExample;
import com.packtpub.reactive.four.ScanExamples;
import com.packtpub.reactive.seven.BlockingExample;
import com.packtpub.reactive.six.BufferingExamples;
import com.packtpub.reactive.six.IntervalAndSchedulers;
import com.packtpub.reactive.six.ParallelRequestsExample;
import com.packtpub.reactive.six.SchedulersExamples;
import com.packtpub.reactive.six.SchedulersTypesExamples;
import com.packtpub.reactive.three.ConnectableObservableExample;
import com.packtpub.reactive.three.ObservableCreateExample;
import com.packtpub.reactive.three.ObservableCreateJustExample;
import com.packtpub.reactive.three.ObservableCreateVariousExample;
import com.packtpub.reactive.three.ObservableCreationFromExamples;
import com.packtpub.reactive.three.ObservableCreationFromFutures;
import com.packtpub.reactive.three.SubjectsExample;

/**
 * The main class of this project it is able to run all the examples.
 * 
 * <p>
 * 	It is a command line application.
 *  You can type commands to the main program and it will respond if they are valid.
 *  <br />
 *  The valid commands are:
 * </p>
 * 
 * <ul>
 * 	<li>
 * 		<b>list</b> - Lists a set of examples and their IDs.
 * 		<ul>
 * 			<li><i>list/list all</i> - Lists all the examples.</li>
 * 			<li><i>list chapter [chapter_number1] [chapter_number2] ...</i> - Lists the examples of only those chapters, provided in the command.</li>
 * 		</ul>
 * 	</li>
 * 	<li>
 * 		<b>run</b> - Runs an by a provided ID.
 * 		<ul>
 * 			<li><i>run [example-id]</i> - Runs the given example. You can see the IDs of the examples with list.</li>
 * 			<li>
 * 				<i>run chapter [chapter_number] [sequential_number_in_chapter] ...</i> - Runs the nth example of the provided chapter.
 * 				<p>
 * 					An example is:
 * 					<pre>
 * 						run chapter 1 2
 * 					</pre>
 * 					This will run the second example in chapter one.
 * 				</p>
 * 			</li>
 * 		</ul>
 * 	</li>
 * </ul>
 * 
 * @author meddle
 */
public class Runner {

	private final List<Program> programs = new ArrayList<Program>();
	private final Map<String, Action1<String[]>> actions = new HashMap<String, Action1<String[]>>();

	/**
	 * Constructs the Runner.
	 */
	public Runner() {
		actions.put("list", this::listPrograms);
		actions.put("run", this::runProgram);
		actions.put("help", this::help);
	}

	/**
	 * Registers programs to be run by this runner application.
	 * 
	 * @param programs
	 * 			The programs to register.
	 */
	public void registerPrograms(Program... programs) {
		this.programs.addAll(Arrays.asList(programs));
	}

	public void help(String... params) {
		this.output(
				"---------------------------------------------------------------------------------------------------------------------",
				"This is a command line program, which runs the examples of the 'Learning Reactive Programming With Java 8' book.",
				"Available commands are : ",
				"  list - Lists the available examples prefixed by their IDs.",
				"      It lists all the examples when run as just 'list' or 'list all'",
				"      It lists only the examples under given chapter(s) when run as 'list chapter <number1> <number2> ...'",
				"      For example 'list chapter 2' will list the examples of chapter 2.",
				"  run - Runs an example.",
				"      It runs an example by given ID (you can look at the IDs by running 'list'), when executed as 'run <ID>'",
				"      For example 'run 5' will run the example with ID of 5.",
				"      It can execute an example by chapter, when run as 'run chapter <chapter_number> <example_number_in_chapter>'",
				"      Example of this is 'run chapter 1 2' - this will run the second example of the first chapter.",
				"  help - Prints this help.",
				"  exit - Exits this program.",
				"---------------------------------------------------------------------------------------------------------------------"
				);
	}
	
	/**
	 * Represents an user action. It is triggered on the <i>list</i> command.
	 * 
	 * @param params
	 * 			Could be:
	 * 			<ul>
	 * 				<li>empty array - All the registered examples, grouped by chapters will be listed.</li>
	 * 				<li>["all"] - The same as above.</li>
	 * 				<li>["chapter", "chapter_number_1", ...] - The examples only of the chapters with the provided numbers will be listed.</li>
	 * 			</ul>
	 */
	public void listPrograms(String... params) {
		Observable<Program> examples = Observable
				.from(this.programs)
				.distinct();

		Observable<String> firstParam = Observable.from(params).first();

		Observable<GroupedObservable<Integer, String>> all = firstParam
		.filter(type -> type.isEmpty() || type.equals("all"))
		.flatMap(v -> examples.compose(new IndexAndGroupChapterExamples()));

		Observable<GroupedObservable<Integer, String>> filteredByChapter = Observable.from(params)
		.skip(1)
		.map(Integer::parseInt)
		.flatMap(param -> examples.compose(new IndexAndGroupChapterExamples())
				.filter(obs -> obs.getKey() == param));
		

		Observable<GroupedObservable<Integer, String>> chapter = firstParam
		.filter(type -> type.equals("chapter"))
		.flatMap(v -> filteredByChapter);
		
		Observable<String> list = Observable.merge(all, chapter)
				.flatMap(obs -> Observable.just("Chapter " + obs.getKey() + " : ").concatWith(obs));
		
		list.subscribe(System.out::println, System.err::println,
						System.out::println);
	}

	/**
	 * Represents an user action. It is triggered on the <i>run</i> command.
	 * 
	 * @param params
	 * 			Could be:
	 * 			<ul>
	 * 				<li>["ID"] - The example with the passed id will be run.</li>
	 * 				<li>["chapter", "chapter_number_1", "number_in_the_chapter"] - The [number_in_the_chapter] example of the given chapter will be run.</li>
	 * 			</ul>
	 */
	public void runProgram(String... params) {
		programFromParams(params)
		.doOnNext(this::wellcome)
		.subscribe(
				program -> program.run(),
				e -> System.err.println(e.getMessage())
				);
	}
	
	/**
	 * Entry point of the Runner.
	 * Uses the input stream to receive and run commands.
	 */
	public void run() {
		Observable<String> input = CreateObservable.input();

		input.flatMap(
				line -> Observable.zip(
						toActionObservable(line),
						toActionParamsObservable(line),
						this::commandToActionRunner)
						)
				.cast(ActionRunner.class)
				.subscribe(action -> action.run(),
						e -> System.err.println(e.getMessage()),
						() -> System.out.println("Bye, bye :)"));
	}

	private Observable<Program> programFromParams(String... params) {
		Observable<Program> simpleRun = Observable
				.from(params)
				.first()
				.filter(this::isNumber)
				.map(this::strToInt)
				.filter(index -> 0 <= index && index < this.programs.size())
				.flatMap(
						index -> Observable.from(this.programs)
								.elementAt(index));

		Observable<Program> programByChapter = Observable
				.from(params)
				.skipWhile(p -> p.equals("chapter"))
				.filter(this::isNumber)
				.map(this::strToInt)
				.buffer(2)
				.filter(a -> a.size() == 2)
				.flatMap(
						indices -> Observable.from(this.programs)
								.groupBy(program -> program.chapter())
								.elementAt(indices.get(0)).flatMap(p -> p)
								.elementAt(indices.get(1)));

		return Observable.concat(simpleRun, programByChapter);
	}

	private int strToInt(String str) {
		return Integer.parseInt(str) - 1;
	}

	private boolean isNumber(String p) {
		return p.matches("[0-9]+");
	}

	public void wellcome(Program program) {
		output("Running " + program.name() + "...");
	}

	private Observable<String> commands(String str) {
		return Observable.from(str.split(" ")).map(command -> command.trim());
	}

	private ActionRunner commandToActionRunner(Action1<String[]> command,
			List<String> args) {
		return new ActionRunner(command, args.toArray(new String[0]));
	}

	private Observable<List<String>> toActionParamsObservable(String line) {
		return commands(line)
				.skip(1)
				.defaultIfEmpty("")
				.<String> flatMap(
						args -> Observable.from(args.split(",")).map(
								arg -> arg.trim())).buffer(9);
	}

	private Observable<Action1<String[]>> toActionObservable(String line) {
		return commands(line).first()
				.filter(action -> actions.containsKey(action))
				.map(action -> actions.get(action));
	}

	public void output(String... messages) {
		Observable.from(messages).subscribe(System.out::println);
	}

	private static Runner init() {
		Runner runner = new Runner();
		runner.registerPrograms(
				new ObservableVSIterator(),
				new ReactiveSumV1(),
				new Java8LambdasSyntaxIntroduction(),
				new ReactiveSumWithLambdas(), new FunctionsExample(),
				new MonadsExample(), new ObservablesAndMonads(),
				new ObservableCreationFromExamples(),
				new ObservableCreationFromFutures(),
				new ObservableCreateJustExample(),
				new ObservableCreateVariousExample(),
				new ObservableCreateExample(),
				new ConnectableObservableExample(), new SubjectsExample(),
				new MappingExamples(), new FlatMapAndFiles(),
				new GroupingExamples(), new OtherTransformationExample(),
				new FilteringExamples(), new ScanExamples(),
				new CombiningObservablesExample(),
				new ConditionalsExample(),
				new HandlingErrorsExamples(),
				new HttpRequestsExample(),
				new IntervalAndSchedulers(),
				new SchedulersTypesExamples(),
				new SchedulersExamples(),
				new ParallelRequestsExample(),
				new BufferingExamples(),
				new BlockingExample(),
				new ResourceManagementExample(),
				new LiftExample(),
				new ComposeExample()
				);

		return runner;
	}

	
	public static void main(String[] args) {
		Runner runner = init();

		runner.output("Example selection interface.",
				"Available commands - 'list', 'help', 'run <ID>', 'exit'");

		runner.run();
	}
}
