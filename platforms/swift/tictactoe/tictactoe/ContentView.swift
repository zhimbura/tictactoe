//
//  ContentView.swift
//  tictactoe
//
//  Created by tikhon on 7/30/22.
//

import SwiftUI
import GAMEFramework

struct ContentView: View {
    @State var buttons: [[String]] = [
        [" ", " ", " "],
        [" ", " ", " "],
        [" ", " ", " "]
    ]
    @State var isGameOver = false
    let game = Game()
    var gameCells: [String: FieldCell] = [:]
    
    var body: some View {
        VStack(alignment: .center) {
            HStack(spacing: 5) {
                // TODO переделать на for
                Group {
                    Button(action: { clickButton(x: 0, y: 0) }) {
                        Text(buttons[0][0])
                    }
                    Button(action: { clickButton(x: 1, y: 0) }) {
                        Text(buttons[0][1])
                    }
                    Button(action: { clickButton(x: 2, y: 0) }) {
                        Text(buttons[0][2])
                    }
                }
                .frame(maxWidth:.infinity)
                .padding()
                .foregroundColor(Color.black)
                .font(.system(size: 64, weight: .bold, design: .default))
                .background(Color(red: 0.654, green: 0.654, blue: 0.776))
                .cornerRadius(5)
            }
            HStack(spacing: 5) {
                Group {
                    Button(action: { clickButton(x: 0, y: 1) }) {
                        Text(buttons[1][0])
                    }
                    Button(action: { clickButton(x: 1, y: 1) }) {
                        Text(buttons[1][1])
                    }
                    Button(action: { clickButton(x: 2, y: 1) }) {
                        Text(buttons[1][2])
                    }
                }
                .frame(maxWidth:.infinity)
                .padding()
                .foregroundColor(Color.black)
                .font(.system(size: 64, weight: .bold, design: .default))
                .background(Color(red: 0.654, green: 0.654, blue: 0.776))
                .cornerRadius(5)
            }
            HStack(spacing: 5) {
                Group {
                    Button(action: { clickButton(x: 0, y: 2) }) {
                        Text(buttons[2][0])
                    }
                    Button(action: { clickButton(x: 1, y: 2) }) {
                        Text(buttons[2][1])
                    }
                    Button(action: { clickButton(x: 2, y: 2) }) {
                        Text(buttons[2][2])
                    }
                }
                .frame(maxWidth:.infinity)
                .padding()
                .foregroundColor(Color.black)
                .font(.system(size: 64, weight: .bold, design: .default))
                .background(Color(red: 0.654, green: 0.654, blue: 0.776))
                .cornerRadius(5)
            }
            Group {
                Text("result")
                Button("again") {
                    game.reset()
                    isGameOver = false
                }
            }.opacity(isGameOver ? 1 : 0)
        }
    }

   
    init () {
        do {
            try game.on(eventType: .changeCell, callBack: {event in
                print(event)
            })
            try game.on(eventType: .gameOver, callBack: {event in
                print(event)
            })
        } catch {
           print("Ошибка")
        }
       
        game.play()
    }
    
    mutating func onChngeCell(event: IEvent) {
        let fieldCell = (event as! GameCellEvent).fieldCell
        let hash = String(fieldCell.getX()) + "-" + String(fieldCell.getY())
        if (!gameCells.keys.contains(hash)) {
            gameCells[hash] = fieldCell
        }
        let x = Int(fieldCell.getX())
        let y = Int(fieldCell.getY())
        switch fieldCell.getState() {
        case .cross:
            buttons[y][x] = "X"
        case .zero:
            buttons[y][x] = "O"
        default:
            buttons[y][x] = " "
        }
    }
    
    mutating func onGameOver() {
        isGameOver = true
        
    }
    
    func clickButton(x: Int, y: Int) {
        let hash = String(x) + "-" + String(y)
        print("Click " + hash)
        gameCells[hash]?.click()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
