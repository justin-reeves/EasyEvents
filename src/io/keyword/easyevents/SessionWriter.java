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
import java.util.Objects;


/**
 * Class Description
 * <p>
 * Created By: Justin Reeves, Pasang Sherpa, Xiaotian Wang (Xander)
 * Created On: 3/26/2021
 */
class SessionWriter {

    //region FIELDS & CONSTRUCTOR & ACCESSOR
    private static final SessionWriter writer = new SessionWriter();

    private SessionWriter() {
    }

    public static SessionWriter getWriter() {
        return writer;
    }
    // endregion


    //region BUSINESS METHODS

    /**
     * write all recorded events in to a file
     * this file consists of:
     * header - session information
     * body - all events {Time Elapsed, Description}
     *
     * @param fileName   session name or customized file name
     * @param type       it can generate multiple file types e.g. txt pdf and csv
     * @param collection the session passes the events collection's referent to this method
     * @throws IOException when I/O encounter issues during writing process
     */
    public static Path writeFile(String fileName, SessionWriter.FileType type, Collection<Event> collection) throws IOException {
        String outputFile = fileName;
        if (Objects.isNull(outputFile) || outputFile.isEmpty()) {
            outputFile = "Session_" + LocalDate.now().toString();
        }
        Path path = Paths.get(System.getProperty("user.dir"), outputFile + type.getFileType());
        if (Files.exists(path)) {
            // add current time at the end of filename if file exists
            path = Paths.get(System.getProperty("user.dir"), outputFile + LocalTime.now().format(DateTimeFormatter.ofPattern("_hh_mm_ss")) + type.getFileType());
        }
        switch (type) {
            case CSV:
                csvFileWriter(path, outputFile, collection);
                break;
            case PDF:
                pdfFileWriter(path, outputFile, collection);
                break;
            case TXT:
                textFileWriter(path, outputFile, collection);
                break;
            default:
                break;
        }
        return path;
    }
    // endregion

    //region HELPER METHODS
    private static void csvFileWriter(Path path, String fileName, Collection<Event> collection) {
        //TODO: generate csv file
    }

    private static void pdfFileWriter(Path path, String fileName, Collection<Event> collection) {
        //TODO: generate pdf file
    }

    private static void textFileWriter(Path path, String fileName, Collection<Event> collection) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(getWriter().sessionHeader(fileName));
            collection.forEach(e -> {
                try {
                    writer.write(String.format("%s\t\t\t%s\n", e.getFormattedEventTimeStamp(), e.getDescription()));
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                }
            });
            writer.write(getWriter().sessionFooter());
        }
    }

    /**
     * @param sessionName given session name from Session who calls writeFile()
     * @return generate a header for output file
     */
    private String sessionHeader(String sessionName) {
        return String.format("Easy Events Session: %s\nCreated on: %s\n\nEvents:\n\nTime Elapsed\t\tEvent Description\n------------\t\t------------\n"
                , sessionName, LocalDate.now());
    }

    /**
     * @return generate a footer for the output file
     */
    private String sessionFooter() {
        return "\n\n\nThanks for using Easy Events!";
    }
    //endregion

    /**
     * provide three different available output file types
     */
    public enum FileType {
        TXT(".txt"),
        CSV(".csv"),
        PDF(".pdf");

        private final String type;

        FileType(String s) {
            this.type = s;
        }

        public String getFileType() {
            return this.type;
        }
    }
}