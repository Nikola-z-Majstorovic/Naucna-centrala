import { Component, OnInit } from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {GenericService} from '../../services/generic/generic.service';
import {Dto} from '../../model/dto';

@Component({
  selector: 'app-author',
  templateUrl: './author.component.html',
  styleUrls: ['./author.component.css']
})
export class AuthorComponent implements OnInit {
  magazineId: number;
  magazines: Dto[];
  private relativeUrlForAllMagazines = '/magazines/activated-by-editor';

  constructor(private genericService: GenericService, private toastr: ToastrService) {
    this.magazines = [];
  }


  ngOnInit(): void {
    this.getAllMagazines();
  }
  getAllMagazines() {
    this.genericService.get<Dto[]>(this.relativeUrlForAllMagazines).subscribe(
      (magazines: Dto[]) => {
        this.magazines = magazines;
        if (this.magazines && this.magazines.length > 0) {
          this.magazineId = this.magazines[0].id;
        }
        this.toastr.success('Magazines loaded!');
      },
      (err) => {
        this.toastr.error('Problem with loading magazines!');
      }
    );
  }
}
