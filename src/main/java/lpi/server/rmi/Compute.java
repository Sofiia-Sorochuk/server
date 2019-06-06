package lpi.server.rmi;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {

    public static final String RMI_SERVER_NAME = "lpi.server.rmi";

    public void ping() throws RemoteException;

    public String echo(String text) throws RemoteException;

    <T> T executeTask(Task<T> t) throws RemoteException, ArgumentException, ServerException;

    public long timeProcesMethod(Sort sort) throws RemoteException;

    public interface Task<T> {
        T execute();
    }

    public static class Sort implements Task<byte[]>, Serializable {
        private static final long serialVersionUID = 227L;

        private String nameRandomFile;
        private String nameSortFile;
        private byte[] fileRandom;
        private byte[] fileSort;
        private Integer[] MasSort;
        public static long startAlgoritm, finishAlgoritm, timeConsumedMilis;


        public Sort(String nameSortFile, File file) throws IOException {
            this.nameSortFile = nameSortFile;
            this.nameRandomFile = file.getName();
            this.fileRandom = Files.readAllBytes(file.toPath());
            parsInfoFromFileRandomIntoMas(fileRandom);
        }

        public Sort(String nameRandomFile, String nameSortFile, byte[] fileRandom) {
            this.nameRandomFile = nameRandomFile;
            this.nameSortFile = nameSortFile;
            this.fileRandom = fileRandom;
            parsInfoFromFileRandomIntoMas(fileRandom);
        }

        private Integer[] parsInfoFromFileRandomIntoMas(byte[] file) {
            String infoFromFile = new String(file);
            String[] numInStingType = infoFromFile.split(" ");
            MasSort = new Integer[numInStingType.length];

            for (int i = 0; i < numInStingType.length; i++) {
                MasSort[i] = Integer.parseInt(numInStingType[i]);
            }
            return MasSort;
        }

        public void Sort() {
            int in, out, t, h=2;


            while (h <= MasSort.length / 3) {
                h = h * 2 + 1;
            }
            while (h > 0) {
                for (out = h; out < MasSort.length; out++) {
                    t = MasSort[out];
                    in = out;
                    while (in > h - 1 && MasSort[in - h] >= t) {
                        MasSort[in] = MasSort[in - h];
                        in -= h;
                    }
                    MasSort[in] = t;
                }
                h = (h - 1) / 3;
            }
        }

        @Override
        public byte[] execute() {
            startAlgoritm = System.nanoTime();
            Sort();
            finishAlgoritm = System.nanoTime();
            timeConsumedMilis = finishAlgoritm - startAlgoritm;
            setTimeConsumedMilis(timeConsumedMilis);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < MasSort.length; i++) {
                if (i-1 != MasSort.length ) {
                    stringBuilder.append(MasSort[i] + " ");
                } else {
                    stringBuilder.append(MasSort[i]);
                }
            }
            return stringBuilder.toString().getBytes();
        }

        public void setTimeConsumedMilis(long timeConsumedMilis) {
            this.timeConsumedMilis = timeConsumedMilis;
        }

        public String getNameRandomFile() {
            return nameRandomFile;
        }

        public void setNameRandomFile(String nameRandomFile) {
            this.nameRandomFile = nameRandomFile;
        }

        public String getNameSortFile() {
            return nameSortFile;
        }

        public void setNameSortFile(String nameSortFile) {
            this.nameSortFile = nameSortFile;
        }

        public byte[] getFileRandom() {
            return fileRandom;
        }

        public void setFileRandom(byte[] fileRandom) {
            this.fileRandom = fileRandom;
        }

        public byte[] getFileSort() {
            return fileSort;
        }

        public void setFileSort(byte[] fileSort) {
            this.fileSort = fileSort;
        }

        public long getStartAlgoritm() {
            return startAlgoritm;
        }

        public long getFinishAlgoritm() {
            return finishAlgoritm;
        }

        public long getTimeConsumedMilis() {
            return timeConsumedMilis;
        }
    }

    public static class Sum implements Task<Integer>, Serializable {
        private static final long serialVersionUID = 228L;

        private final Integer a;
        private final Integer b;

        public Sum(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public Integer execute() {
            return a + b;
        }
    }

    public static class ServerException extends RemoteException {
        private static final long serialVersionUID = 2592458695363000913L;

        public ServerException() {
            super();
        }

        public ServerException(String message, Throwable cause) {
            super(message, cause);
        }

        public ServerException(String message) {
            super(message);
        }
    }

    public static class ArgumentException extends RemoteException {
        private static final long serialVersionUID = 8404607085051949404L;

        private String argumentName;

        public ArgumentException() {
            super();
        }

        public ArgumentException(String argumentName, String message, Throwable cause) {
            super(message, cause);
            this.argumentName = argumentName;
        }

        public ArgumentException(String argumentName, String message) {
            super(message);
            this.argumentName = argumentName;
        }


        public String getArgumentName() {
            return argumentName;
        }
    }

}
