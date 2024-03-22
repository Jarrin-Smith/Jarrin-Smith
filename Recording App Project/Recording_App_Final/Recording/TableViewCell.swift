//
//  TableViewCell.swift
//  Recording
//
//  Created by Jarrin Smith (jks7@iu.edu) and Ethan Stone (ethstone@iu.edu)
//  Recording App
//  4/28/23.
//

import UIKit

class TableViewCell: UITableViewCell {

    @IBOutlet weak var playButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
