// Código completo de la clase Inicio con las nuevas integraciones
package servicios;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import dtos.PalaDto;
import dtos.PelotaDto;

/**
 * Clase principal que implementa la lógica del juego y la interfaz de usuario.
 * 
 * @author amorcia
 * @version 1.0
 * @date 22/01/2025
 */
public class OperativaImplementacion extends JFrame implements OperativaInterfaz {
    private CardLayout cardLayout; // Gestor de tarjetas para cambiar entre paneles
    private PanelMenu panelMenu; // Panel del menú principal
    private PanelJuego panelJuego; // Panel del juego

    /**
     * Constructor que inicializa la ventana del juego y sus componentes.
     * 
     * @date 22/01/2025 - amorcia
     */
    public OperativaImplementacion() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Juego de las Palas");

        panelMenu = new PanelMenu(this);
        panelJuego = new PanelJuego(this);

        add(panelMenu, "Menú");
        add(panelJuego, "Juego");

        cardLayout.show(getContentPane(), "Menu");

        setPreferredSize(new Dimension(950, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void iniciar(int nivel) {
        panelJuego.iniciarJuego(nivel);
        cardLayout.show(getContentPane(), "Juego");
    }

    @Override
    public void mostrarMenu() {
        panelJuego.reiniciar();
        cardLayout.show(getContentPane(), "Menú");
    }

    @Override
    public void mostrarDialogo(int nivel, int puntuacion, int tiempoPasado) {
        // Verifica si se han alcanzado los objetivos de puntuación
        if (nivel == 1 && panelJuego.getContadorLadrillosRotos() >= 30) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has roto los 30 ladrillos en el nivel 1.");
            gameOver();
        } else if (nivel == 2 && panelJuego.getContadorLadrillosRotos() >= 50) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has roto los 50 ladrillos en el nivel 2.");
            gameOver();
        }
    }

    @Override
    public void gameOver() {
        panelJuego.gameOver();
    }

    /**
     * Clase que representa el panel del menú principal del juego.
     * 
     * @author amorcia
     * @date 22/01/2025
     */
    class PanelMenu extends JPanel {
        /**
         * Constructor que inicializa el panel del menú.
         * 
         * @param frame Referencia a la interfaz del juego.
         * @date 22/01/2025 -amorcia
         */
        public PanelMenu(OperativaInterfaz frame) {
            setLayout(new GridBagLayout());
            setBackground(Color.BLACK);

            JLabel titulo = new JLabel("INICIO");
            titulo.setFont(new Font("Arial", Font.BOLD, 36));
            titulo.setForeground(Color.WHITE);

            JButton botonModoBasico = new JButton("Básico");
            JButton botonModoMedio = new JButton("Intermedio");

            botonModoBasico.setFont(new Font("Arial", Font.PLAIN, 18));
            botonModoBasico.setForeground(Color.WHITE);
            botonModoBasico.setBackground(Color.BLACK);
            botonModoBasico.setFocusPainted(false);
            botonModoBasico.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            botonModoMedio.setFont(new Font("Arial", Font.PLAIN, 18));
            botonModoMedio.setForeground(Color.WHITE);
            botonModoMedio.setBackground(Color.BLACK);
            botonModoMedio.setFocusPainted(false);
            botonModoMedio.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            // Acción para iniciar el juego en modo básico
            botonModoBasico.addActionListener(e -> frame.iniciar(1));
            // Acción para iniciar el juego en modo intermedio
            botonModoMedio.addActionListener(e -> frame.iniciar(2));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 20, 10);
            gbc.anchor = GridBagConstraints.CENTER;

            add(titulo, gbc);
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(botonModoBasico, gbc);
            gbc.gridy = 2;
            add(botonModoMedio, gbc);
        }
    }

    /**
     * Clase que representa el panel del juego donde ocurre la lógica del mismo.
     * 
     * @author amorcia
     * @date 22/01/2025
     */
    class PanelJuego extends JPanel implements ActionListener {
        private Timer timer; // Temporizador principal del juego
        private Timer temporizadorLadrillo; // Temporizador para regenerar ladrillos
        private Timer temporizadorJuego; // Temporizador para el tiempo del juego
        private PalaDto pala; // Pala del jugador
        private PalaDto pala2; // Segunda pala para el modo intermedio
        private PelotaDto pelota; // Pelota del juego
        private final int ANCHO_PALA = 100; // Ancho de la pala
        private final int ALTO_PALA = 15; // Alto de la pala
        private final int VOLUMEN_PELOTA = 20; // Volumen (diámetro) de la pelota
        public List<Rectangle2D> ladrillos; // Lista de ladrillos
        private int puntuacion = 0; // Puntuación del jugador
        private int vidas = 3; // Vidas restantes del jugador
        private int nivel; // Nivel actual del juego
        public List<Rectangle2D> ladrillosRotos; // Lista de ladrillos rotos
        private int segundos = 0; // Segundos transcurridos
        private final int tiempoModoBasico = 600; // Tiempo límite en modo básico
        private final int tiempoModoMedio = 300; // Tiempo límite en modo intermedio
        private JButton botonVolverAtras; // Botón para volver al menú
        private boolean isGameOver = false; // Bandera que indica si el juego ha terminado

        // Contadores de ladrillos
        private int contadorLadrillosRotos = 0; // Contador de ladrillos rotos
        private final int maximoLadrillosRotos = 100; // Máximo de ladrillos rotos

        /**
         * Constructor que inicializa el panel del juego.
         * 
         * @param frame Referencia a la interfaz del juego.
         * @date 22/01/2025 - amorcia
         */
        public PanelJuego(OperativaInterfaz frame) {
            setBackground(Color.BLACK);
            setFocusable(true);
            // Controlador de movimiento del mouse para mover la pala
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    pala.mover(e.getX() - ANCHO_PALA / 2);
                    pala2.mover(pala.getPosicion().getX()); // Sincronizar con la pala inferior
                }
            });

            botonVolverAtras = new JButton("X");
            botonVolverAtras.addActionListener(e -> {
                reiniciarTemporizador();
                frame.mostrarMenu();
            });

            JPanel botonPanel = new JPanel(new BorderLayout());
            botonPanel.setOpaque(false);
            botonPanel.add(botonVolverAtras, BorderLayout.EAST);

            setLayout(new BorderLayout());
            add(botonPanel, BorderLayout.NORTH);
        }

        /**
         * Inicia el juego con el nivel especificado.
         * 
         * @param level Nivel del juego a iniciar.
         * @date 22/01/2025 - amorcia
         */
        public void iniciarJuego(int level) {
            this.nivel = level;
            this.puntuacion = 0;
            this.vidas = (level == 0) ? 0 : 3; // Asignar vidas según el nivel
            this.ladrillos = new ArrayList<>();
            this.ladrillosRotos = new ArrayList<>();
            this.segundos = 0;
            this.contadorLadrillosRotos = 0; // Reiniciar contador de ladrillos rotos

            pala = new PalaDto(getWidth() / 2 - ANCHO_PALA / 2, getHeight() - ALTO_PALA - 30, ANCHO_PALA, ALTO_PALA);
            pala2 = new PalaDto(getWidth() / 2 - ANCHO_PALA / 2, 50, ANCHO_PALA, ALTO_PALA); // Pala superior en modo medio

            int velocidadPelota = (level == 1) ? 2 : 4; // Velocidad de la pelota según el nivel
            pelota = new PelotaDto(getWidth() / 2, 400, VOLUMEN_PELOTA, velocidadPelota, -velocidadPelota);

            generarLadrillos(); // Generación de ladrillos

            reiniciarTemporizador();
            timer.start(); // Iniciar el temporizador del juego
            iniciarTemporizadorLadrillos(); // Iniciar el temporizador de ladrillos
        }

        /**
         * Reinicia el estado del juego.
         * 
         * @date 22/01/2025 - amorcia
         */
        public void reiniciar() {
            isGameOver = false;
            puntuacion = 0;
            vidas = (nivel == 0) ? 0 : 3; // Reinicia vidas según el nivel
            segundos = 0;
            contadorLadrillosRotos = 0; // Reiniciar contador de ladrillos rotos
            reiniciarTemporizador();
            repaint(); // Redibuja el panel
        }

        /**
         * Reinicia los temporizadores del juego.
         * 
         * @date 22/01/2025 - amorcia
         */
        private void reiniciarTemporizador() {
            if (timer != null) {
                timer.stop(); // Detener el temporizador principal
            }
            if (temporizadorJuego != null) {
                temporizadorJuego.stop(); // Detener el temporizador del juego
            }
            if (temporizadorLadrillo != null) {
                temporizadorLadrillo.stop(); // Detener el temporizador de ladrillos
            }

            timer = new Timer(10, this); // Temporizador del juego
            temporizadorJuego = new Timer(1000, e -> {
                segundos++; // Incrementar el contador de segundos
                // Verificar si se ha alcanzado el tiempo límite
                if ((nivel == 1 && segundos >= tiempoModoBasico) || (nivel == 2 && segundos >= tiempoModoMedio)) {
                    gameOver();
                }
                repaint(); // Redibuja el panel
            });

            temporizadorJuego.start(); // Iniciar el temporizador del juego
        }

        /**
         * Inicia el temporizador para regenerar ladrillos.
         * 
         * @date 22/01/2025 - amorcia
         */
        private void iniciarTemporizadorLadrillos() {
            // Temporizador para regenerar ladrillos según el nivel
            if (nivel == 1) {
                temporizadorLadrillo = new Timer(120000, e -> regenerarLadrillos(5));
            } else {
                temporizadorLadrillo = new Timer(15000, e -> regenerarLadrillos(5));
            }
            temporizadorLadrillo.start(); // Iniciar el temporizador de ladrillos
        }

        /**
         * Genera los ladrillos en el panel del juego.
         * 
         * @date 22/01/2025 - amorcia
         */
        private void generarLadrillos() {
            int filas = 4; // Número de filas de ladrillos
            int anchoLadrillo = 110; // Ancho de cada ladrillo
            int altoLadrillo = 20; // Alto de cada ladrillo
            int espacioX = 25; // Espacio horizontal entre ladrillos
            int espacioY = 15; // Espacio vertical entre ladrillos

            // Bucle para crear ladrillos en el panel
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < filas; j++) {
                    double xPosition = i * (anchoLadrillo + espacioX) + 150; // Posición x
                    double yPosition = j * (altoLadrillo + espacioY) + 210; // Posición y
                    ladrillos.add(new Rectangle2D.Double(xPosition, yPosition, anchoLadrillo, altoLadrillo)); // Añadir ladrillo
                }
            }
        }

        /**
         * Regenera los ladrillos rotos según el contador proporcionado.
         * 
         * @param contador Número de ladrillos a regenerar.
         * @date 22/01/2025 - amorcia
         */
        private void regenerarLadrillo() {
            if (ladrillosRotos.isEmpty()) {
                return; // Si no hay ladrillos destruidos, sale del método
            }
            int randomIndex = (int) (Math.random() * ladrillosRotos.size()); // Selecciona un ladrillo destruido aleatoriamente
            Rectangle2D ladrilloDestruido = ladrillosRotos.get(randomIndex); // Obtiene el ladrillo destruido
            ladrillos.add(new Rectangle2D.Double(ladrilloDestruido.getX(), ladrilloDestruido.getY(), ladrilloDestruido.getWidth(), ladrilloDestruido.getHeight())); // Regenera el ladrillo
            ladrillosRotos.remove(randomIndex); // Elimina el ladrillo de la lista de destruidos
        }

        /**
         * Método para regenerar múltiples ladrillos.
         * 
         * @param cuenta Cantidad de ladrillos a regenerar.
         * @date 22/01/2025 - amorcia
         */
        private void regenerarLadrillos(int cuenta) {
            for (int i = 0; i < cuenta; i++) { // Regenera un ladrillo si no se ha alcanzado el máximo permitido
                if (nivel == 1 && contadorLadrillosRotos < 30) {
                    regenerarLadrillo();
                } else if (nivel == 2 && contadorLadrillosRotos < 45) {
                    regenerarLadrillo();
                }
            }
        }

        /**
         * Maneja el fin del juego y muestra el diálogo correspondiente.
         * 
         * @date 22/01/2025 - amorcia
         */
        public void gameOver() {
            if (isGameOver) return; // Ya está en estado de fin de juego
            isGameOver = true; // Cambia el estado a fin de juego
            timer.stop(); // Detiene el temporizador
            temporizadorLadrillo.stop(); // Detiene el temporizador de ladrillos
            temporizadorJuego.stop(); // Detiene el temporizador del juego
            String message = "El juego acabó. ¿Quieres volver a jugar?";
            int response = JOptionPane.showConfirmDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                iniciarJuego(nivel); // Reinicia el juego
                isGameOver = false; // Cambia el estado a activo
            } else {
                ((OperativaInterfaz) SwingUtilities.getWindowAncestor(this)).mostrarMenu(); // Volver al menú
            }
        }

        /**
         * Obtiene la puntuación actual del jugador.
         * 
         * @return Puntuación actual.
         * @date 22/01/2025 - amorcia
         */
        public int obtenerPuntuacion() {
            return puntuacion; // Devuelve la puntuación
        }

        /**
         * Obtiene el tiempo transcurrido desde el inicio del juego.
         * 
         * @return Segundos transcurridos.
         * @date 22/01/2025 - amorcia
         */
        public int obtenerTiempoTranscurrido() {
            return segundos; // Devuelve el tiempo transcurrido
        }

        /**
         * Obtiene la cantidad de ladrillos rotos.
         * 
         * @return Número de ladrillos rotos.
         * @date 22/01/2025 - amorcia
         */
        public int getContadorLadrillosRotos() {
            return contadorLadrillosRotos; // Devuelve el número de ladrillos rotos
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int ancho = getWidth();
            int alto = getHeight();

            // Dibuja la pala inferior
            g.setColor(Color.BLUE);
            g.fillRect(pala.getPosicion().getX(), alto - ALTO_PALA - 30, pala.getAncho(), pala.getAlto());

            // Dibuja la pala superior
            if (nivel == 2) {
                g.setColor(Color.BLUE);
                g.fillRect(pala2.getPosicion().getX(), pala2.getPosicion().getY(), pala2.getAncho(), pala2.getAlto());
            }

            // Dibuja la pelota
            g.setColor(Color.WHITE);
            g.fillOval(pelota.getPosicion().getX(), pelota.getPosicion().getY(), VOLUMEN_PELOTA, VOLUMEN_PELOTA);

            // Dibuja los ladrillos
            g.setColor(Color.RED);
            for (Rectangle2D ladrillo : ladrillos) {
                g.fillRect((int) ladrillo.getX(), (int) ladrillo.getY(), (int) ladrillo.getWidth(), (int) ladrillo.getHeight());
            }

            // Dibuja la puntuación y las vidas
            g.setColor(Color.WHITE);
            g.drawString("Puntuación: " + puntuacion, 10, 20);
            if (vidas > 0) {
                g.drawString("Vidas: " + vidas, 10, 40);
            }

            // Dibuja el temporizador
            int minutos = segundos / 60;
            int restoSegundos = segundos % 60;
            g.drawString(String.format("Tiempo: %02d:%02d", minutos, restoSegundos), ancho / 2 - 30, 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pelota.mover(); // Mueve la pelota

            // Colisiones con los bordes
            if (pelota.getPosicion().getX() < 0 || pelota.getPosicion().getX() > getWidth() - VOLUMEN_PELOTA) {
                pelota.rebotarHorizontal(); // Rebote horizontal
            }

            // Manejar la caída de la pelota
            if (pelota.getPosicion().getY() > getHeight()) {
                if (vidas > 0) {
                    vidas--; // Reduce vidas
                    puntuacion -= 5; // Penaliza puntuación
                    if (vidas <= 0) {
                        gameOver(); // Fin del juego si no hay vidas
                    } else {
                        reiniciarPelota(); // Reinicia la pelota
                    }
                }
            }

            // Colisión con el techo (nuevo comportamiento en ambos modos)
            if (pelota.getPosicion().getY() < 0) {
                if (nivel == 2) {
                    vidas--; // Reduce vidas en modo intermedio
                    puntuacion -= 5; // Penaliza puntuación
                    if (vidas <= 0) {
                        gameOver(); // Fin del juego si no hay vidas
                    } else {
                        reiniciarPelota(); // Reinicia la pelota
                    }
                } else {
                    pelota.rebotarVertical(); // Rebote vertical
                    pelota.getPosicion().setY(0); // Asegúrate de que no pase el límite
                }
            }

            // Colisión con la pala inferior
            if (pelota.getPosicion().getY() >= getHeight() - ALTO_PALA - VOLUMEN_PELOTA - 30 && pelota.getPosicion().getX() >= pala.getPosicion().getX() && pelota.getPosicion().getX() <= pala.getPosicion().getX() + pala.getAncho()) {
                pelota.rebotarVertical(); // Rebote vertical
                pelota.getPosicion().setY(getHeight() - ALTO_PALA - VOLUMEN_PELOTA - 30); // Colocación correcta
            }

            // Colisión con la pala superior
            if (nivel == 2 && pelota.getPosicion().getY() <= pala2.getPosicion().getY() + ALTO_PALA && pelota.getPosicion().getX() >= pala2.getPosicion().getX() && pelota.getPosicion().getX() <= pala2.getPosicion().getX() + pala2.getAncho()) {
                pelota.rebotarVertical(); // Rebote vertical
                pelota.getPosicion().setY(pala2.getPosicion().getY() + ALTO_PALA); // Colocación correcta
            }

            // Colisiones con los ladrillos
            for (int i = 0; i < ladrillos.size(); i++) {
                Rectangle2D ladrillo = ladrillos.get(i);
                if (ladrillo.intersects(pelota.getPosicion().getX(), pelota.getPosicion().getY(), VOLUMEN_PELOTA, VOLUMEN_PELOTA)) {
                    ladrillos.remove(i); // Elimina el ladrillo golpeado
                    ladrillosRotos.add(ladrillo); // Añade a la lista de ladrillos rotos
                    contadorLadrillosRotos++; // Incrementar el contador de ladrillos rotos
                    System.out.println("Ladrillos Rotos: " + contadorLadrillosRotos);
                    pelota.rebotarVertical(); // Rebote vertical
                    puntuacion += 10; // Aumenta puntuación

                    // Mostrar diálogo si se alcanza el puntaje específico
                    mostrarDialogo(nivel, puntuacion, segundos);

                    break; // Salir del bucle tras colisión
                }
            }

            repaint(); // Redibuja el panel
        }

        /**
         * Reinicia la pelota a su posición inicial.
         * 
         * @date 22/01/2025 - amorcia
         */
        private void reiniciarPelota() {
            pelota = new PelotaDto(getWidth() / 2, 400, VOLUMEN_PELOTA, (nivel == 1) ? 2 : 4, -((nivel == 1) ? 2 : 4)); // Reinicia la pelota
        }
    }
}




