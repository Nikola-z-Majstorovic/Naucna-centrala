import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ValidationService} from '../../services/validation.service';
import {RepositoryService} from '../../services/repository.service';

@Component({
  selector: 'app-reviewer',
  templateUrl: './reviewer.component.html',
  styleUrls: ['./reviewer.component.css']
})
export class ReviewerComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {

  }

}
