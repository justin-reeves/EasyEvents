package io.keyword.easyevents;



import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;


/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class SessionWriter {

    // FIELDS
    private static final SessionWriter writer = new SessionWriter();

    // CONSTRUCTORS
    private SessionWriter(){}

    public static SessionWriter getWriter(){
        return writer;
    }

    // BUSINESS METHODS
    /**
     * write all recorded events in to a file
     * this file consists of:
     * header - session information
     * body - all events {Time Elapsed, Description}
     * @param fileName
     * @param type
     * @param collection
     * @throws IOException
     */
    public static void writeFile(String fileName, SessionWriter.FileType type , Collection<Event> collection) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), fileName + type.getFileType());
        if(Files.exists(path)){
            // add current time if file exists
            path = Paths.get(System.getProperty("user.dir"), fileName + LocalTime.now().format(DateTimeFormatter.ofPattern("_hh_mm_ss")) + type.getFileType());
        }

        try(BufferedWriter writer = Files.newBufferedWriter(path)){
            writer.write(getWriter().sessionHeader(fileName));
            collection.stream().forEach(e -> {
                try {
                    writer.write(String.format("%s\t\t\t%s\n", e.getFormattedEventTimeStamp(), e.getDescription()));
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                }
            });
            writer.write(getWriter().sessionFooter());
        }
    }

    // ACCESSOR METHODS


    // HELPER METHODS
    private String sessionHeader(String sessionName){
        return String.format("Easy Events Session: %s\nCreated on: %s\n\nEvents:\n\nTime Elapsed\t\tEvent Description\n------------\t\t------------\n"
                ,sessionName, LocalDate.now());
    }

    private String sessionFooter(){
        return "\n\n\nThanks for using Easy Events!";
    }

    // OVERRIDES
    /**
     * provide three different available output file types
     */
    public enum FileType{
        TXT(".txt"),
        CSV(".csv"),
        PDF(".pdf");

        private String type;
        FileType(String s) {
            this.type = s;
        }

        public String getFileType(){
            return this.type;
        }
    }
}