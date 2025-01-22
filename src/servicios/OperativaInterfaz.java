package servicios;

public interface OperativaInterfaz {

	void iniciar(int nivel);

	void mostrarMenu();

	void mostrarDialogo(int nivel, int puntuacion, int tiempoPasado);

	void gameOver();

}