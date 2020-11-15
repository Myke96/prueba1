package buscaminas;
import java.io.*;
public class buscaminas {

	// ################################################################################
	// Función max: Función auxiliar que calcula el máximo de dos números
	// ################################################################################
	public static int max(int num1, int num2) {
		int num;
		if (num1>num2) {
			num = num1;
		} else {
			num = num2;
		}
		return num;
	}

	// ################################################################################
	// Función min: Función auxiliar que calcula el mínimo de dos números
	// ################################################################################
	public static int min(int num1, int num2) {
		int num;
		if (num1<num2) {
			num = num1;
		} else {
			num = num2;
		}
		return num;
	}

	// ################################################################################
	// Función IncializarTablero: Función que recibe las dos talbas: tablero, visible
	// Incializa tablero con valores 0
	// Incializa visible con valores Falso, indicando que están ocultas
	// ################################################################################
	public static void inicializartablero(int[][] tablero, boolean visible[][]) {
		int colum;
		int fila;
		// Incializo el tablero con valor 0
		for (fila=0;fila<=9;fila++) {
			for (colum=0;colum<=9;colum++) {
				tablero[fila-1][colum-1] = 0;
			}
		}
		ponerminas(tablero);
		// Incializo la tabla visible a falso indicando que ninguna celda está descubierta
		for (fila=0;fila<=9;fila++) {
			for (colum=0;colum<=9;colum++) {
				visible[fila-1][colum-1] = false;
			}
		}
	}

	// ################################################################################
	// Función PonerMinas: Función que recibe el tablero (tabla 10x10) por referencia
	// Genera 10 posiciones de la tabla e incializa esas posiciones con minas (valor 9)
	// Debe asegurar que se ponen 10 minas.
	// Cada vez que se pone una mina se incrementa en 1 el valor de las celdas vecinas,
	// si no son una mina
	// ################################################################################
	public static void ponerminas(int[][] tablero) {
		int colum;
		int colum2;
		int fila;
		int fila2;
		int num_minas;
		num_minas = 0;
		// Vamos a poner 10 minas en el tablero
		while (num_minas<10) {
			// Me aseguro que no haya ya una mina en la posición que se genera aleatoriamente
			do {
				fila = (int) Math.floor(Math.random()*10);
				colum = (int) Math.floor(Math.random()*10);
			} while (tablero[fila-1][colum-1]==9);
			// Reperesentamos la mina con el número 9
			tablero[fila-1][colum-1] = 9;
			// Ahora incremento el número de minas cercanas en las casillas vecinas
			for (fila2=max(0,fila-1);fila2<=min(9,fila+1);fila2++) {
				for (colum2=max(0,colum-1);colum2<=min(9,colum+1);colum2++) {
					if (tablero[fila2-1][colum2-1]!=9) {
						tablero[fila2-1][colum2-1] = tablero[fila2-1][colum2-1]+1;
					}
				}
			}
			num_minas = num_minas+1;
		}
	}

	// ################################################################################
	// Función DestaparCelda: Funcion que recibe por referencia las dos tablas y la
	// fila y columna que se debe destapar.
	// Si es una casilla que se puede destapar (la posición de la tabla visible es Falso)
	// Se destapa (posición de la tabla visible a Verdadero)
	// Si no hay minas cerca tengo que intentar destapar las vecinas
	// Si la celda vecina no es una mina, la destapo
	// Función recursiva
	// ################################################################################
	public static void destaparcelda(int[][] tablero, boolean visible[][], int fila, int colum) {
		int colum2;
		int fila2;
		// Si es una casilla que se puede destapar
		if (visible[fila-1][colum-1]==false) {
			visible[fila-1][colum-1] = true;
			// Si no hay minas cerca tengo que intentar destapar las vecinas
			if (tablero[fila-1][colum-1]==0) {
				for (fila2=max(0,fila-1);fila2<=min(9,fila+1);fila2++) {
					for (colum2=max(0,colum-1);colum2<=min(9,colum+1);colum2++) {
						if (tablero[fila2-1][colum2-1]!=9) {
							destaparcelda(tablero,visible,fila2,colum2);
						}
					}
				}
			}
		}
	}

	// ################################################################################
	// Función ContarCeldasDestapadas: Funcion que recibe la tabla visible
	// Recorre la tabla y cuenta los valores Verdaderos, este valor se devuelve
	// Si el contador es 90 significa que hemos destapado todas las casillas: hemos ganado
	// ################################################################################
	public static int contarceldasdestapadas(boolean visible[][]) {
		int colum;
		int fila;
		int num;
		num = 0;
		for (fila=0;fila<=9;fila++) {
			for (colum=0;colum<=9;colum++) {
				if (visible[fila-1][colum-1]) {
					num = num+1;
				}
			}
		}
		return num;
	}

	// ################################################################################
	// Función ComprobarTablero: Funcion que recibe por referencia las dos tablas y la
	// fila y columna que se debe destapar.
	// Si la posición a destapar es una mina (=9) devuelve -1 (hemos perdido)
	// SiNo destapo la casilla correspondiente y cuento las casillas detapadas y se devuelve
	// ################################################################################
	public static int comprobartablero(int[][] tablero, boolean visible[][], int fila, int colum) {
		int resultado;
		// Si es una mina devuelvo -1
		if (tablero[fila-1][colum-1]==9) {
			// Destapo la celda con la mina
			visible[fila-1][colum-1] = true;
			resultado = -1;
		} else {
			destaparcelda(tablero,visible,fila,colum);
			resultado = contarceldasdestapadas(visible);
		}
		return resultado;
	}

	// ################################################################################
	// Función EscribirTablero: Funcion que las dos tablas 
	// Recorre las tablas y las muestras en pantalla
	// Dependiendo del valor de cada posición de la tabla visible, muestra la posición
	// de la tabla resultado.
	// Si la posición está destapada (verdadero):
	// Si no tiene minas alrededor (valor 0) muestra un hueco
	// Si es una mina, muestro un *
	// SiNo muetro el valor de la casilla (indica cuantas minas tiene alrededor)
	// SiNo la posición no es visible y muestro un #
	// ################################################################################
	public static void escribirtablero(int[][] tablero, boolean visible[][]) {
		int colum;
		int fila;
		String titcolum;
		String titfilas;
		titfilas = "0123456789";
		titcolum = "    0 1 2 3 4 5 6 7 8 9";
		System.out.println(titcolum);
		System.out.println("");
		// Recorro las tablas
		for (fila=0;fila<=9;fila++) {
			System.out.print(titfilas.substring(fila-1,fila)+"   ");
			for (colum=0;colum<=9;colum++) {
				// Si la celda es visible (está destapada)
				if (visible[fila-1][colum-1]) {
					// Celda que no tiene minas alrededor
					if (tablero[fila-1][colum-1]==0) {
						System.out.print("  ");
					} else {
						// Es una mina
						if (tablero[fila-1][colum-1]==9) {
							System.out.print("* ");
							// Muestro el número de minas que hay en los vecinos 
						} else {
							System.out.print(tablero[fila-1][colum-1]+" ");
						}
					}
					// La casilla no es visible
				} else {
					System.out.print("# ");
				}
			}
			System.out.println("");
		}
	}

	// ################################################################################
	// Programa BuscaMina
	// Incilizo las tablas: tablero y visible
	// Se repite:
	// Mostrar el tablero
	// Pedir fila y columna de casilla a destapar
	// Comprobar tablero
	// Hasta que la comprobación = -1 (has perdido hay una mina)
	// O hasta que haya destapada todas las casillas (Has ganado)
	// ################################################################################
	public static void main(String args[]) throws IOException {
		BufferedReader bufEntrada = new BufferedReader(new InputStreamReader(System.in));
		int colum;
		int fila;
		int resultado;
		int tablero[][];
		boolean visible[][];
		tablero = new int[10][10];
		visible = new boolean[10][10];
		inicializartablero(tablero,visible);
		do {
			escribirtablero(tablero,visible);
			// Pedir fila y columna de casilla a destapar
			do {
				System.out.print("Indica fila:");
				fila = Integer.parseInt(bufEntrada.readLine());
			} while (!(fila>=0 && fila<=9));
			do {
				System.out.print("Indica columna:");
				colum = Integer.parseInt(bufEntrada.readLine());
			} while (!(colum>=0 && colum<=9));
			// Comprobamos el tablero
			resultado = comprobartablero(tablero,visible,fila,colum);
			System.out.println(""); // no hay forma directa de borrar la consola en Java
		} while (!(resultado==-1 || resultado==90));
		escribirtablero(tablero,visible);
		// Has destapado una mina
		if (resultado==-1) {
			System.out.println("Has pisado una mina!!!!!");
			System.out.println("GAME OVER");
			// has destapado todas las casillas
		} else {
			System.out.println("YOU ARE THE PLAYER ONE!!!");
		}
	}


}

