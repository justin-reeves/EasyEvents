# EasyEvents
An event-logging solution for video recordings

### About
Some screen recording software does not provide specific timestamp markings which make for easy viewing of specific events during that video. If the software does automatically provide the capability, it is not always accurate or over “logs” events, e.g. consider the Adobe Connect event log when viewing class recordings. 

EasyEvents aims to provide a way to log video recording notes (events) in tandem with a video recording. We allow users, via the command line, to input event notes which are then marked with a timestamp relative to a given start time. These events are then logged to an output file alongside a URL which brings the viewer of the notes to the specified timestamp in the video recording. This provides for a user-specified granularity of event descriptions and frequency and makes for reviewing specific topics easier and faster than painstakingly skipping through a recording. 

#### Basic Idea
1.	(Outside of EasyEvents): A video recording starts (via third party software - i.e., Adobe Connect, NVIDIA Screen Record, etc.)
2.	EasyEvents is started simultaneously, creating an event log.
  a.	On start, initial time recorded.
  b.	Until recording stopped: prompt user to enter an event.
    i.	Ask for event description.
    ii.	Log description along with a time elapsed (recordings timestamp) relative to start time.
3.	Ask for recording URL.
4.	Attach timestamp to URL via a [query parameter?]
5.	Output recording data to file [.txt, .csv, etc.]
  a.	User can specify file output name OR
  b.	Default name created.
6.	(Outside of EasyEvents): Any user can then open the file and skip straight to a specific event.

#### Project Structure

The **src** folder contains all necessary ***Java*** class files for running this program.

The **test** folder contains all tests relative to ***Java*** classes in the **src** folder.

##### Notes
This project was created for the TLG Learning Software Development Engineer course.

