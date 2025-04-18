Okay, I need to develop a Java program for tracking watched multimedia content across different platforms. The user wants a CLI interface with several options. Let's break down the requirements.

First, the main actions are adding, removing, marking as watched with a rating, and displaying content. The multimedia can be either movies or series, each with specific attributes. Also, platforms are predefined, and entries must be unique.

So, the first step is to model the classes. Since movies and series share some attributes but have others, I should create a superclass, maybe MultimediaContent, with common fields like title, platform. Then Movie and Serie can extend this. The platform should be an enum to restrict the possible values.

For uniqueness, each content must be checked before adding. So overriding equals and hashCode in the subclasses would help. That way, when adding to a Set, duplicates are automatically handled.

Next, the main logic needs to track two lists: "Estoy viendo" (currently watching) and "Vistas" (watched). But since movies and series are separate, maybe have separate lists for each? Or maybe have a single list with a status (watching/watched) and type. Hmm, perhaps better to have separate collections for each state and type. Or maybe have a class that holds the content along with its status and rating. Wait, but when moving from watching to watched, the rating is added. So perhaps the watched list entries need to include the rating. So maybe have two separate structures: one for currently watching (without ratings) and one for watched (with ratings). And both can contain either movies or series.

So, the data structures could be:

- A Set<MultimediaContent> for currently watching. Using a Set to avoid duplicates. Wait, but when moving to watched, it's removed from here. But the watched list would have ratings. Hmm, maybe have a different structure for watched items, like a list of RatedContent objects that have the content and the rating. But then, the RatedContent would need to reference the original content. Alternatively, maybe track the content in two separate sets: one for watching (without ratings) and another for watched (with ratings). But how to store the rating? So perhaps, for watched items, we need to have a wrapper class that includes the content and its rating. Let's call it WatchedContent, which has a MultimediaContent and an int rating. Then, the watched list can be a list of WatchedContent objects.

But for uniqueness, when adding to watched, we need to check if the content is already there. But since when you mark as watched, you remove it from the currently watching list. So when you mark as watched, the content is moved from the current list to the watched list, with a rating. So each content can exist either in current or in watched, not both. So maybe the current watching is a Set<MultimediaContent>, and the watched is a Set<WatchedContent>, where WatchedContent contains the MultimediaContent and the rating.

That makes sense. Then, when adding a new content, we check if it's in either current or watched. If not, add to current. When marking as watched, remove from current, add to watched with rating.

So the next step is designing the classes:

- MultimediaContent (abstract class or interface) with title, platform. Then Movie and Serie as subclasses. Maybe make it abstract with a method like getDetails() or something that each subclass implements for display.

Wait, but the display format varies between movies and series. So each subclass can override a method to return their specific details. For example, when displaying in the current list, movies show duration, series show seasons and episode type. So maybe a method like displayCurrentInfo() and displayWatchedInfo(int rating) which returns the formatted string.

Alternatively, use toString() but that's not flexible enough. Maybe better to have separate methods for the different display formats.

Then, the enums: Platform as an enum with the options given. For the episode length in series, maybe a boolean for short or long, or an enum. The user's requirement says "Capítulos: Cortos o largos", so maybe an enum EpisodeLength { SHORT, LONG }.

So the Serie class would have numSeasons and episodeLength (enum). Movie has duration (int, minutes).

Now, handling the CLI. The main program will have a loop presenting the menu options. Each option corresponds to a method. Need to read user input, validate it, and perform actions.

For input validation, ensure that when adding a movie, duration is positive. For series, numSeasons positive, episode length is valid. Platforms must be selected from the enum. Ratings 1-5.

To avoid code duplication, perhaps create helper methods for reading input with validation. For example, a method that prompts the user, reads an integer within a range, etc.

Now, data structures:

- Current watching: Set<MultimediaContent> (since Set ensures uniqueness)

- Watched: List<WatchedContent>, where WatchedContent has MultimediaContent and rating. Wait, but when displaying watched movies or series, they need to be sorted by rating. So storing them in a list that can be sorted.

Wait, but when displaying movies and series in watched separately, perhaps it's better to have separate lists for movies and series in watched. Or perhaps have a single list but filter by type. Hmm, but when we need to sort movies by rating descending, and series by rating descending. So maybe store all watched content in a list of WatchedContent objects, and when displaying, filter by type (instanceof Movie or Serie) and then sort.

Alternatively, have two separate lists: one for watched movies and one for watched series. But that could complicate things. Maybe better to have a single list of WatchedContent, which can be filtered and sorted as needed.

The WatchedContent class would look like:

class WatchedContent {

private MultimediaContent content;

private int rating;

// constructor, getters

}

Then, when displaying movies, filter the list to get all WatchedContent where content is an instance of Movie, then sort by rating. Similarly for series.

But how to sort them in descending order of rating. So for the list of watched contents, when we need to show movies, collect all WatchedContent where content is Movie, then sort them by their rating in reverse order. Same for series.

For handling this, the WatchedContent class could implement Comparable based on rating, but since we need separate sorting for movies and series, perhaps it's better to use a custom comparator when sorting the filtered lists.

So, for example:

List<WatchedContent> watched = ...;

// For movies:

List<WatchedContent> watchedMovies = watched.stream()

.filter(wc -> wc.getContent() instanceof Movie)

.sorted(Comparator.comparingInt(WatchedContent::getRating).reversed())

.collect(Collectors.toList());

Same for series.

Now, the main program structure:

- Main class with main method.

- Classes for handling data: maybe a MediaManager class that holds the current and watched lists, and methods to add, remove, mark as watched, etc.

- CLI class that handles user interaction, input/output, and calls the MediaManager methods.

But for simplicity, maybe the main class can handle the CLI and the data, but better to separate concerns.

So perhaps:

- Model package:

- MultimediaContent (abstract)

- Movie (extends MultimediaContent)

- Serie (extends MultimediaContent)

- Platform (enum)

- EpisodeLength (enum: CORTO, LARGO)

- WatchedContent (wrapper class)

- Service package:

- MediaManager: handles the current and watched sets/lists, with methods for adding, removing, etc.

- Main class: handles CLI loop, input/output, uses MediaManager.

Now, the MediaManager needs to:

- Add a multimedia content to current (checking for duplicates in both current and watched)

- Remove from current

- Mark as watched (move from current to watched, add rating)

- Get current list (to display)

- Get watched movies sorted by rating

- Get watched series sorted by rating

For checking duplicates, since when adding, a content is considered duplicate if it exists in current or in watched (even if it's in watched with a rating). Because the user shouldn't add the same content again.

Wait, the requirement says: "No se debería permitir que una serie o película esté repetida, es decir, no debería de aparecer dos veces en el programa con exactamente los mismos datos."

So same data: same title, platform, and other attributes. So two movies with same title, platform, and duration are considered duplicates. Similarly, two series with same title, platform, seasons, and episode length are duplicates.

Thus, the equals method in Movie and Serie must check all attributes. So in MultimediaContent, equals is based on title and platform. But wait, that's not enough. Because two movies could have same title and platform but different durations. So equals should check all attributes specific to the subclass.

So for Movie:
```
@Override

public boolean equals(Object o) {

if (this == o) return true;

if (o == null || getClass() != o.getClass()) return false;

Movie movie = (Movie) o;

return duration == movie.duration &&

title.equals(movie.title) &&

platform == movie.platform;

}

Similarly for Serie:

@Override

public boolean equals(Object o) {

if (this == o) return true;

if (o == null || getClass() != o.getClass()) return false;

Serie serie = (Serie) o;

return numSeasons == serie.numSeasons &&

title.equals(serie.title) &&

platform == serie.platform &&

episodeLength == serie.episodeLength;

}
```

And hashCode should be overridden accordingly.

So when adding a new Movie, check if it exists in current or in any of the watched contents' content. So the MediaManager needs to check both current and watched for duplicates.

But how to check if a content is in watched? Because watched is a list of WatchedContent objects. So for each wc in watched, check if wc.getContent().equals(newContent). So in MediaManager:
```
public boolean containsContent(MultimediaContent content) {

if (currentWatching.contains(content)) {

return true;

}

for (WatchedContent wc : watched) {

if (wc.getContent().equals(content)) {

return true;

}

}

return false;

}
```

Then, when adding, check if containsContent is true. If so, reject.

Now, the CLI steps:

Option 1: Add new movie or series.

The user selects movie or series. Then inputs title, platform (from list), and specific attributes. For movie: duration. For series: numSeasons and episodeLength.

Need to validate all inputs. For platform, present the options (Netflix, MAX, etc.), maybe as a numbered list, user selects the number. For episodeLength, also as a choice (Corto/Largo).

Option 2: Remove from current. User inputs title and platform, then the type (movie/series) and other attributes to identify. Or perhaps list the current contents and let the user choose by number. But since it's a CLI, maybe the user has to input the exact data. But that could be error-prone. Alternatively, show the current list and let the user pick an index. But in a CLI, displaying the list first when choosing to delete may complicate the flow. Alternatively, the user must know exactly which entry to delete by providing all details, which is not user-friendly. Hmm. Maybe when choosing to delete, the user is prompted for the title, platform, and type (movie/series), then other attributes. Then check if such a content exists in currentWatching. If so, remove it.

But how to handle cases where multiple entries have same title and platform but different other attributes? Wait, no, because the equals method checks all attributes. So the user must input all the details exactly as when added. But this could be tedious. For example, deleting a movie requires entering title, platform, duration again. But perhaps in practice, the user would first list the current items (option 4), note the details, then choose to delete by entering those details. It's a bit cumbersome but manageable.

Alternatively, when adding, each entry is given an ID, and deletion is done by ID. But the requirements don't mention that, so perhaps stick to the original approach.

Option 3: Mark as watched. Similar to deletion: user provides details of the content in current, then a rating. The content is moved to watched with the rating.

Options 4,5,6: Display lists. For current, just list all. For watched, list movies or series sorted by rating.

Now, the CLI flow for each option.

For example, adding a movie:

1. Choose movie or series.

2. Enter title.

3. Select platform from the enum options (user inputs a number corresponding to the option).

4. For movie: enter duration (must be positive).

5. Check if this movie already exists (using containsContent). If not, add to currentWatching.

So, in code, the CLI would prompt step by step.

Now, for error handling:

- When entering platform, if the user inputs an invalid number, prompt again.

- Duration must be positive.

- For series: numSeasons must be positive, episodeLength must be valid (corto/largo).

- Rating must be between 1-5.

The MediaManager methods should throw exceptions or return boolean for success/failure, and the CLI handles the messages.

But perhaps the MediaManager returns a status (e.g., DUPLICATE_CONTENT, INVALID_RATING, etc.), and the CLI displays appropriate error messages.

But for simplicity, maybe have the MediaManager methods return a boolean (true if added, false if duplicate), and the CLI checks and informs the user.

Now, code structure.

First, the model classes.

MultimediaContent.java (abstract)
```
public abstract class MultimediaContent {

protected String title;

protected Platform platform;

public MultimediaContent(String title, Platform platform) {

this.title = title;

this.platform = platform;

}

public String getTitle() {

return title;

}

public Platform getPlatform() {

return platform;

}

// Abstract methods for details

public abstract String getCurrentDetails();

public abstract String getWatchedDetails(int rating);

// equals and hashCode must be overridden in subclasses

@Override

public abstract boolean equals(Object o);

@Override

public abstract int hashCode();

}

Movie.java:

public class Movie extends MultimediaContent {

private int duration;

public Movie(String title, Platform platform, int duration) {

super(title, platform);

this.duration = duration;

}

public int getDuration() {

return duration;

}

@Override

public String getCurrentDetails() {

return String.format("Película: %s\nPlataforma: %s\nDuración: %d", title, platform, duration);

}

@Override

public String getWatchedDetails(int rating) {

String stars = "*".repeat(rating);

return String.format("Película: %s\nPlataforma: %s\nDuración: %d\nValoración: %s", title, platform, duration, stars);

}

@Override

public boolean equals(Object o) {

if (this == o) return true;

if (o == null || getClass() != o.getClass()) return false;

Movie movie = (Movie) o;

return duration == movie.duration &&

title.equals(movie.title) &&

platform == movie.platform;

}

@Override

public int hashCode() {

return Objects.hash(title, platform, duration);

}

}

Serie.java:

public class Serie extends MultimediaContent {

private int numSeasons;

private EpisodeLength episodeLength;

public Serie(String title, Platform platform, int numSeasons, EpisodeLength episodeLength) {

super(title, platform);

this.numSeasons = numSeasons;

this.episodeLength = episodeLength;

}

public int getNumSeasons() {

return numSeasons;

}

public EpisodeLength getEpisodeLength() {

return episodeLength;

}

@Override

public String getCurrentDetails() {

return String.format("Serie: %s\nPlataforma: %s\nTemporadas: %d\nCapítulos: %s", title, platform, numSeasons, episodeLength.toString().toLowerCase());

}

@Override

public String getWatchedDetails(int rating) {

String stars = "*".repeat(rating);

return String.format("Serie: %s\nPlataforma: %s\nTemporadas: %d\nCapítulos: %s\nValoración: %s", title, platform, numSeasons, episodeLength.toString().toLowerCase(), stars);

}

@Override

public boolean equals(Object o) {

if (this == o) return true;

if (o == null || getClass() != o.getClass()) return false;

Serie serie = (Serie) o;

return numSeasons == serie.numSeasons &&

title.equals(serie.title) &&

platform == serie.platform &&

episodeLength == serie.episodeLength;

}

@Override

public int hashCode() {

return Objects.hash(title, platform, numSeasons, episodeLength);

}

}

Platform.java (enum):

public enum Platform {

NETFLIX, MAX, AMAZON, DISNEY, OTROS;

}

EpisodeLength.java (enum):

public enum EpisodeLength {

CORTO, LARGO;

}

WatchedContent.java:

public class WatchedContent {

private MultimediaContent content;

private int rating;

public WatchedContent(MultimediaContent content, int rating) {

this.content = content;

this.rating = rating;

}

public MultimediaContent getContent() {

return content;

}

public int getRating() {

return rating;

}

}

Now, the MediaManager class:

import java.util.*;

public class MediaManager {

private Set<MultimediaContent> currentWatching;

private List<WatchedContent> watched;

public MediaManager() {

currentWatching = new HashSet<>();

watched = new ArrayList<>();

}

public boolean addContent(MultimediaContent content) {

if (containsContent(content)) {

return false;

}

return currentWatching.add(content);

}

public boolean removeContent(MultimediaContent content) {

return currentWatching.remove(content);

}

public boolean markAsWatched(MultimediaContent content, int rating) {

if (currentWatching.remove(content)) {

watched.add(new WatchedContent(content, rating));

return true;

}

return false;

}

public List<MultimediaContent> getCurrentWatching() {

return new ArrayList<>(currentWatching);

}

public List<WatchedContent> getWatchedMovies() {

return watched.stream()

.filter(wc -> wc.getContent() instanceof Movie)

.sorted((wc1, wc2) -> Integer.compare(wc2.getRating(), wc1.getRating()))

.collect(Collectors.toList());

}

public List<WatchedContent> getWatchedSeries() {

return watched.stream()

.filter(wc -> wc.getContent() instanceof Serie)

.sorted((wc1, wc2) -> Integer.compare(wc2.getRating(), wc1.getRating()))

.collect(Collectors.toList());

}

private boolean containsContent(MultimediaContent content) {

if (currentWatching.contains(content)) {

return true;

}

for (WatchedContent wc : watched) {

if (wc.getContent().equals(content)) {

return true;

}

}

return false;

}

}
```

Wait, the methods getWatchedMovies and getWatchedSeries should return sorted lists. So using streams to filter and sort.

Now, the CLI part. The main class will handle the user interaction. Let's call it MediaTrackerApp.

But how to read input. Using Scanner.

The steps for each option:

Option 1: Add content.

- Ask if it's a movie or series.

- Read title.

- Select platform from the enum options.

- For movie: read duration (positive).

- For series: read numSeasons (positive), select episode length (corto/largo).

- Create the object and add to current via MediaManager.

But how to present the platform options. For example:

Platform options:

1. Netflix

2. MAX

3. Amazon

4. Disney

5. Otros

User enters a number 1-5. Then map to the enum.

Similarly for episode length: 1. Corto, 2. Largo.

So helper methods for selecting enums:
```

private static Platform selectPlatform(Scanner scanner) {

System.out.println("Selecciona la plataforma:");

Platform[] platforms = Platform.values();

for (int i = 0; i < platforms.length; i++) {

System.out.printf("%d. %s\n", i+1, platforms[i].name());

}

int choice = readIntInRange(scanner, 1, platforms.length);

return platforms[choice-1];

}
```

Similarly for episodeLength.

The helper method readIntInRange could handle input validation, reprompting until a valid integer in the range is entered.

Another helper method for reading a positive integer.

Now, the main loop in MediaTrackerApp:
```
public class MediaTrackerApp {

private static MediaManager mediaManager = new MediaManager();

private static Scanner scanner = new Scanner(System.in);

public static void main(String[] args) {

boolean exit = false;

while (!exit) {

printMenu();

int option = readIntInRange(scanner, 1, 7);

switch (option) {

case 1:

addContent();

break;

case 2:

removeContent();

break;

case 3:

markAsWatched();

break;

case 4:

showCurrent();

break;

case 5:

showWatchedMovies();

break;

case 6:

showWatchedSeries();

break;

case 7:

exit = true;

break;

default:

System.out.println("Opción inválida.");

}

}

System.out.println("¡Hasta luego!");

}

private static void printMenu() {

System.out.println("1. Añadir contenido");

System.out.println("2. Eliminar contenido de 'Estoy viendo'");

System.out.println("3. Marcar como vista y valorar");

System.out.println("4. Mostrar contenido en 'Estoy viendo'");

System.out.println("5. Mostrar películas vistas ordenadas por valoración");

System.out.println("6. Mostrar series vistas ordenadas por valoración");

System.out.println("7. Salir");

System.out.print("Selecciona una opción: ");

}

private static int readIntInRange(Scanner scanner, int min, int max) {

while (true) {

try {

int input = scanner.nextInt();

scanner.nextLine(); // consume newline

if (input < min || input > max) {

System.out.printf("Por favor, introduce un número entre %d y %d: ", min, max);

} else {

return input;

}

} catch (InputMismatchException e) {

System.out.printf("Entrada inválida. Introduce un número entre %d y %d: ", min, max);

scanner.nextLine(); // consume invalid input

}

}

}

private static int readPositiveInt(Scanner scanner, String prompt) {

System.out.print(prompt);

while (true) {

try {

int input = scanner.nextInt();

scanner.nextLine();

if (input <= 0) {

System.out.print("El valor debe ser positivo. Inténtalo de nuevo: ");

} else {

return input;

}

} catch (InputMismatchException e) {

System.out.print("Entrada inválida. Introduce un número positivo: ");

scanner.nextLine();

}

}

}

private static void addContent() {

System.out.println("¿Qué tipo de contenido deseas añadir?");

System.out.println("1. Película");

System.out.println("2. Serie");

int typeChoice = readIntInRange(scanner, 1, 2);

System.out.print("Introduce el título: ");

String title = scanner.nextLine().trim();

Platform platform = selectPlatform(scanner);

MultimediaContent content;

if (typeChoice == 1) {

int duration = readPositiveInt(scanner, "Introduce la duración en minutos: ");

content = new Movie(title, platform, duration);

} else {

int seasons = readPositiveInt(scanner, "Introduce el número de temporadas: ");

EpisodeLength episodeLength = selectEpisodeLength(scanner);

content = new Serie(title, platform, seasons, episodeLength);

}

if (mediaManager.addContent(content)) {

System.out.println("Contenido añadido correctamente.");

} else {

System.out.println("Error: El contenido ya existe.");

}

}

private static EpisodeLength selectEpisodeLength(Scanner scanner) {

System.out.println("Selecciona la duración de los capítulos:");

System.out.println("1. Cortos");

System.out.println("2. Largos");

int choice = readIntInRange(scanner, 1, 2);

return choice == 1 ? EpisodeLength.CORTO : EpisodeLength.LARGO;

}

// Similarly, implement removeContent, markAsWatched, etc.

}
```

The removeContent() function would need to prompt the user for the details of the content to remove. But how to get the exact content from currentWatching.

This could be tricky. One approach is to list all current contents and let the user select by index. For example:
```
private static void removeContent() {

List<MultimediaContent> current = mediaManager.getCurrentWatching();

if (current.isEmpty()) {

System.out.println("No hay contenido en 'Estoy viendo'.");

return;

}

System.out.println("Contenido en 'Estoy viendo':");

for (int i = 0; i < current.size(); i++) {

MultimediaContent content = current.get(i);

System.out.printf("%d. %s (%s)\n", i+1, content.getTitle(), content.getClass().getSimpleName());

}

System.out.print("Selecciona el número del contenido a eliminar: ");

int choice = readIntInRange(scanner, 1, current.size());

MultimediaContent toRemove = current.get(choice-1);

if (mediaManager.removeContent(toRemove)) {

System.out.println("Contenido eliminado correctamente.");

} else {

System.out.println("Error al eliminar el contenido.");

}

}
```
But this requires that the getCurrentWatching() returns a list in a predictable order. Since currentWatching is a HashSet, the order is not guaranteed. So perhaps change currentWatching to a LinkedHashSet to preserve insertion order. Then, when converting to a list, the order is maintained. So in MediaManager:
```
public MediaManager() {

currentWatching = new LinkedHashSet<>();

watched = new ArrayList<>();

}

public List<MultimediaContent> getCurrentWatching() {

return new ArrayList<>(currentWatching);

}

This way, the order is preserved, and the user can select by index.

Similarly for markAsWatched():

private static void markAsWatched() {

List<MultimediaContent> current = mediaManager.getCurrentWatching();

if (current.isEmpty()) {

System.out.println("No hay contenido en 'Estoy viendo'.");

return;

}

System.out.println("Contenido en 'Estoy viendo':");

for (int i = 0; i < current.size(); i++) {

MultimediaContent content = current.get(i);

System.out.printf("%d. %s (%s)\n", i+1, content.getTitle(), content.getClass().getSimpleName());

}

System.out.print("Selecciona el número del contenido a marcar como visto: ");

int choice = readIntInRange(scanner, 1, current.size());

MultimediaContent toMark = current.get(choice-1);

System.out.print("Introduce la valoración (1-5): ");

int rating = readIntInRange(scanner, 1, 5);

if (mediaManager.markAsWatched(toMark, rating)) {

System.out.println("Contenido marcado como visto y valorado.");

} else {

System.out.println("Error: El contenido no está en 'Estoy viendo'.");

}

}

Then, the display functions:

private static void showCurrent() {

List<MultimediaContent> current = mediaManager.getCurrentWatching();

if (current.isEmpty()) {

System.out.println("No hay contenido en 'Estoy viendo'.");

return;

}

System.out.println("Contenido en 'Estoy viendo':");

for (MultimediaContent content : current) {

System.out.println(content.getCurrentDetails());

System.out.println(); // separator

}

}

private static void showWatchedMovies() {

List<WatchedContent> watchedMovies = mediaManager.getWatchedMovies();

if (watchedMovies.isEmpty()) {

System.out.println("No hay películas vistas.");

return;

}

System.out.println("Películas vistas ordenadas por valoración:");

for (WatchedContent wc : watchedMovies) {

System.out.println(wc.getContent().getWatchedDetails(wc.getRating()));

System.out.println();

}

}

private static void showWatchedSeries() {

List<WatchedContent> watchedSeries = mediaManager.getWatchedSeries();

if (watchedSeries.isEmpty()) {

System.out.println("No hay series vistas.");

return;

}

System.out.println("Series vistas ordenadas por valoración:");

for (WatchedContent wc : watchedSeries) {

System.out.println(wc.getContent().getWatchedDetails(wc.getRating()));

System.out.println();

}

}
```
Wait, but getWatchedDetails is called on the content, passing the rating. But the content's method expects a rating. So in WatchedContent, perhaps we need to call getWatchedDetails with the rating stored.

But the getWatchedDetails method is part of the MultimediaContent, which requires the rating. However, in the current design, the WatchedContent holds the rating. So, for example, when displaying a watched movie, we need to call content.getWatchedDetails(wc.getRating()).

Yes. So in showWatchedMovies, for each WatchedContent wc:

System.out.println(wc.getContent().getWatchedDetails(wc.getRating()));

This should output the correct details with the rating stars.

This way, the Movie and Serie classes handle their own formatting based on the rating.

Testing this, for example, a Movie with rating 3 would display "***".

Now, edge cases:

- Adding duplicate content: checked via containsContent.

- Marking a content as watched that is not in current: MediaManager's markAsWatched checks if it was removed from current, returns false if not.

- Displaying empty lists.

Potential issues:

- The use of LinkedHashSet for currentWatching may have performance implications, but for a small-scale app, it's acceptable.

- The equals and hashCode methods in the subclasses must correctly check all attributes to avoid duplicates.

- The user must input exact data when removing or marking as watched, but using the index selection method simplifies this.

Now, putting it all together, the code should handle all the required functionality. Testing each option thoroughly would be needed to ensure all validations work