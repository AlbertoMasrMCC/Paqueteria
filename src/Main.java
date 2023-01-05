import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static GoogleAPI googleAPI = new GoogleAPI();
    static RutaDAO rutaDAO = new RutaDAO();
    static Point2D destinoFinal = null;
    static ArrayList<Point2D> destinos = new ArrayList<>();
    static ArrayList<Point2D> caminosVisitados = new ArrayList<>();
    static double heuristicaRecorrida = 0;

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        String calleInicial = "Avenida Juan de Dios Bátiz";
        String latitudInicial = "24.789470939797397";
        String longitudInicial = "-107.39676833876051";

        String[] esquinaCercanaInicial = rutaDAO.obtenerEsquinaCercana(latitudInicial, longitudInicial);

        Point2D nodoInicial = new Point2D.Double(Double.parseDouble(esquinaCercanaInicial[1]), Double.parseDouble(esquinaCercanaInicial[2]));

        destinos.add(nodoInicial);

        System.out.print("Ingrese direccion a entregar paquete: ");
        String direccionEntregaPaquete = entrada.nextLine();

        String[] coordenadas = googleAPI.obtenerCoordenadasReales(direccionEntregaPaquete);
        String latitud = coordenadas[0];
        String longitud = coordenadas[1];

        String[] esquinaCercana = rutaDAO.obtenerEsquinaCercana(latitud, longitud);

        destinoFinal = new Point2D.Double(Double.parseDouble(esquinaCercana[1]), Double.parseDouble(esquinaCercana[2]));

        aEstrella(destinos);

    }

    private static void aEstrella(ArrayList<Point2D> frontera) {

        if(frontera.size() == 0) {

            System.out.println("No hay frontera");
            return;

        }

        Point2D edoActual = frontera.remove(0);

        caminosVisitados.add(edoActual);

        if(esMeta(edoActual)) {

            System.out.println("Se encontro la meta");
            mostrarCaminoRecorrido();
            return;

        }

        ArrayList<Point2D> sucesores = obtenerSucesores(edoActual);

        frontera = agregarSucesores(sucesores, frontera);

        frontera = ordenarSucesores(frontera, edoActual);

        aEstrella(frontera);

    }

    private static boolean esMeta(Point2D edoActual) {

        double latitud = edoActual.getX();
        double longitud = edoActual.getY();
        double latitudMeta = destinoFinal.getX();
        double longitudMeta = destinoFinal.getY();

        if(latitud == latitudMeta && longitud == longitudMeta) {

            return true;

        }

        return false;

    }

    // Método para calcular la distancia entre dos puntos con el algoritmo de Haversine
    private static double heuristica(Point2D puntoA, Point2D puntoB) {

        double lat1 = puntoA.getX();
        double lat2 = puntoB.getX();
        double lon1 = puntoA.getY();
        double lon2 = puntoB.getY();

        //convertir grados a radianes
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        //diferencia de latitudes y longitudes
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        //calculo de la distancia
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distancia = 6371 * c;
        return distancia;

    }

    private static ArrayList<Point2D> obtenerSucesores(Point2D edoActual) {

        double latitud = edoActual.getX();
        double longitud = edoActual.getY();

        ArrayList<String[]> vecinos = rutaDAO.obtenerVecinos(Double.toString(latitud), Double.toString(longitud));

        ArrayList<Point2D> sucesores = new ArrayList<>();

        for(String[] vecino : vecinos) {

            Point2D punto = new Point2D.Double(Double.parseDouble(vecino[1]), Double.parseDouble(vecino[2]));

            if(!caminosVisitados.contains(punto)) {

                sucesores.add(punto);

            }

        }

        return sucesores;

    }

    private static ArrayList<Point2D> ordenarSucesores(ArrayList<Point2D> frontera, Point2D edoActual) {

        ArrayList<Point2D> fronteraOrdenada = new ArrayList<>();

        while(frontera.size() > 0) {

            Point2D puntoMenor = frontera.get(0);
            int indice = 0;

            for(int i = 0; i < frontera.size(); i++) {

                Point2D punto = frontera.get(i);

                if(caminosVisitados.contains(punto)) {

                    frontera.remove(i);
                    continue;

                }

                if(heuristica(punto, destinoFinal) + heuristica(edoActual, punto) + heuristicaRecorrida < heuristica(puntoMenor, destinoFinal) + heuristica(edoActual, puntoMenor) + heuristicaRecorrida) {

                    indice = i;

                }

            }

            fronteraOrdenada.add(frontera.remove(indice));

        }

        heuristicaRecorrida += heuristica(edoActual, fronteraOrdenada.get(0));

        return fronteraOrdenada;

    }

    private static ArrayList<Point2D> agregarSucesores(ArrayList<Point2D> sucesores, ArrayList<Point2D> frontera) {

        for(Point2D sucesor : sucesores) {

            if (sucesor == null) {
                continue;
            }

            if (frontera.contains(sucesor)) {
                continue;
            }

            if (caminosVisitados.contains(sucesor)) {
                continue;
            }

            frontera.add(sucesor);

        }

        return frontera;

    }

    private static void mostrarCaminoRecorrido() {

        for (Point2D punto : caminosVisitados) {

            System.out.println(punto);

        }

    }

}
