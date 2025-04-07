import java.io.*;
import java.util.ArrayList;

/**
 * The type Westminster skin consultation manager.
 */
public class WestminsterSkinConsultationManager implements SkinConsultationManager, Serializable {

    /**
     * The constant skinSystem.
     */
    public static WestminsterSkinConsultationManager skinSystem = new WestminsterSkinConsultationManager();
    /**
     * The Doctors.
     */
    private static final ArrayList<Doctor> doctors = new ArrayList<>();
    private static final int MAX_DOCTORS = 10;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */

    public static void main(String[] args) {
        GUI gui = new GUI();
        skinSystem.loadData();

        while (true) {
            int menuNumber = displayMenu();
            switch (menuNumber) {
                case 100 -> skinSystem.addDoctor();
                case 101 -> skinSystem.deleteDoctor();
                case 102 -> skinSystem.printDoctor();
                case 103 -> gui.homepage();
                case 999 -> {
                    skinSystem.saveData();
                    System.exit(0);
                }
                default -> System.out.println("Enter the Menu Number Correct");
            }

        }

    }


    /**
     * <h2>Display Menu
     * <p>In this Method it will Display the Menu Which is Displaying the Number of Doctors it can add. And this Method Which will take an Input From the User as the Menu Number.
     * @return will be the Menu number that was taken from the User.
     */
    private static int displayMenu() {
        if (doctors.size() < MAX_DOCTORS) {
            System.out.println("\n\n100 -   Add Doctor [Can add "+ (MAX_DOCTORS - (doctors.size())) + " More Doctors]");
        } else {
            System.out.println("***  The Number of Doctors is Constrained to Its Maximum 10/10***");
        }
        System.out.println("""
                101 -   Delete Doctor
                102 -   Show Doctors
                103 -   Open GUI
                999 -   Exit
                Enter the Correct Menu Number to Continue
                """);
        return InputValidator.getInt();
    }




    /**
     * <h2>Add a Doctor
     * <p>This Method will add a Doctor to the Doctor Array Using the Data Provided by the User and Validating them using Other Support Methods.
     */
    public void addDoctor() {

        if (doctors.size() < MAX_DOCTORS) {

            String name = InputValidator.getString("First Name","Names");
            String sureName = InputValidator.getString("Surname","Names");
            String dob = InputValidator.getDate();
            String phoneNumber = InputValidator.getString( "Phone Number","Phone");
            int medicalLicenceNumber = InputValidator.getMedNo();
            String specialization = InputValidator.getString("Specialization","Names");
            doctors.add(new Doctor(name, sureName, dob, phoneNumber, medicalLicenceNumber, specialization));

        } else System.out.println("Enter the Menu Number Correct");

    }



    /**
     * <h2>Delete a Doctor
     * <p> When this Method is Called The User will be asked for the Medical Registrations Number of the Doctor that
     * needed to be Removed and the Doctor will be Deleted. And the Details of the Deleted Doctor will be Displayed.
     */
    public void deleteDoctor() {

        int count = 0;
        for (Doctor details : doctors) {
            count++;
            System.out.println("Doctor " + count + "    - " + details.getName() + " " + details.getSurname() + "" +
                    "   - " + details.getMedicalLicenceNumber());
        }

        System.out.println("Enter the Medical Reg No to Remove the Doctor :");
        int regNo = InputValidator.getInt();
        int counter = 0;

        for (int i = 0; i < doctors.size(); i++) {
            if (regNo == doctors.get(i).getMedicalLicenceNumber()) {
                showDoctor(doctors.get(i), i + 1, 4);
                System.out.println("------ Above Mentioned Doctor Has Been Removed ------\n\n");
                doctors.get(i).RemoveData();
                doctors.remove(i);
                break;
            } else counter = counter + 1;
            if (counter == doctors.size()) {
                System.out.println("No Medical Reg No Found");
            }

        }

    }



    /**
     * <h2>Print Doctor
     * <p>In this Method The Surnames will be copied to an array and the array will be sorted and the Sorted array will
     * be Compared with the Doctor array and the Print Function will be Called according to the Order of the Sorted names.
     */
    public void printDoctor() {
        ArrayList<String> surnameArray = new ArrayList<>();
        for (Doctor details : doctors) {
            surnameArray.add(details.getSurname());
        }
        surnameArray.sort(String::compareTo);

        for (String s : surnameArray) {
            for (int j = 0; j < doctors.size(); j++) {
                if (s.equals(doctors.get(j).getSurname())) {
                    showDoctor(doctors.get(j), j + 1, 5);
                }
            }
        }
        surnameArray.clear();
    }




    /**
     * <h2>Save Data to a File
     * <p>This code is saving a list of Doctor objects to a file called "database.txt" using Java's
     * ObjectOutputStream class. It loops through each object in the list and writes it to the file,
     * and then closes the stream when finished. If an error occurs while saving the data, it is caught and a message
     * is printed to the console.In this Method Data Will be Saved using Serialization
     */

    public void saveData() {
        try {

            ObjectOutputStream objectOutputForDoctors = new ObjectOutputStream(new FileOutputStream("database.txt"));

            for (Doctor details : doctors) {
                objectOutputForDoctors.writeObject(details);
            }
            objectOutputForDoctors.close();
            System.out.println(" Doctors Saved" );


            ObjectOutputStream objectOutputForPatients = new ObjectOutputStream(new FileOutputStream("patientDetails.txt"));
            for (Patient details : ConsultationManager.patients) {
                objectOutputForPatients.writeObject(details);
            }
            objectOutputForPatients.close();
            System.out.println(" Patients Saved ");

            ObjectOutputStream objectOutputForConsultations = new ObjectOutputStream(new FileOutputStream("consultationDetails.txt"));
            for (Consultation details : ConsultationManager.consultations) {
                objectOutputForConsultations.writeObject(details);
            }
            objectOutputForConsultations.close();
            System.out.println(" Consultations Saved ");


        } catch (IOException e) {
            System.out.println("*****   Error When Saving Data   *****");

        }

    }


    /**
     * <h2>Loading Data From A file
     * <p>The code is trying to read in serialized objects of type Doctor from a file called "database.txt" and add
     * them to an ArrayList called doctors. It does this by using a FileInputStream to read bytes from the file and an
     * ObjectInputStream to deserialize the bytes into objects. The code uses a try-catch block to handle any exceptions
     * that might occur while reading the data from the file. If there are no more bytes available to read from the file,
     * the code closes the ObjectInputStream and exits the loop.
     */
    public void loadData() {
        doctors.clear();

        try {
            FileInputStream fileInput = new FileInputStream("database.txt");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            while (fileInput.available() > 0) {
                doctors.add((Doctor) objectInput.readObject());
            }

            objectInput.close();

            System.out.println(" Doctors Loaded ");



            FileInputStream fileInputForPatients = new FileInputStream("patientDetails.txt");
            ObjectInputStream objectInputForPatients = new ObjectInputStream(fileInputForPatients);

            while (fileInputForPatients.available() > 0) {
                ConsultationManager.patients.add((Patient) objectInputForPatients.readObject());
            }

            objectInputForPatients.close();

            System.out.println(" Patients Loaded ");


            FileInputStream fileInputForConsultations = new FileInputStream("consultationDetails.txt");
            ObjectInputStream objectInputForConsultations = new ObjectInputStream(fileInputForConsultations);

            while (fileInputForConsultations.available() > 0) {
                ConsultationManager.consultations.add((Consultation) objectInputForConsultations.readObject());
            }

            objectInputForConsultations.close();

            System.out.println(" Consultations Loaded ");



        } catch (IOException | ClassNotFoundException e) {
            System.out.println("*****   Error When Loading Data   *****");
        }

    }

    /**
     * <h2>Printing Doctors
     * <p>This Method Will Print the Details of Doctors According to the number of needed Information
     * @param details This will have the Doctor Objects
     * @param number The Doctor Number will be Passed Using this Parameter
     * @param detailCount How Many Details are Needed is passed Through this Parameter
     */
    private void showDoctor(Doctor details, int number, int detailCount) {

        String[] Data = {"Fist Name           -   ", "Surname             -   ", "Date of Birth       -   ",
                "Phone Number        -   ", "Medical Reg No      -   ", "Specialization      -   "};
        String[] content = {details.getName(), details.getSurname(), details.getDob(), details.getMobileNumber(),
                String.valueOf(details.getMedicalLicenceNumber()), details.getSpecialisation()};
        System.out.println("\n---------------     Doctor " + number + " Details     ---------------");
        for (int i = 0; i < detailCount; i++) {
            System.out.println(Data[i] + content[i]);
        }

    }

    public static ArrayList<Doctor> getArray(){
        return doctors;
    }
}